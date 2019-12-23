package ca.uqtr.tp3.persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.uqtr.tp3.di.annotation.Log;
import ca.uqtr.tp3.di.annotation.Transactional;
import ca.uqtr.tp3.persistence.interfaces.EntityManager;
import ca.uqtr.tp3.persistence.metadata.JoinInfo;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import ca.uqtr.tp3.persistence.exception.ForeignKeyNotFoundException;
import ca.uqtr.tp3.persistence.exception.MissingTableAnnotation;
import ca.uqtr.tp3.persistence.exception.NoPrimaryKeyFoundException;
import ca.uqtr.tp3.persistence.exception.PersitenceException;
import ca.uqtr.tp3.persistence.exception.PrimaryKeyColumnNotFoundException;
import ca.uqtr.tp3.persistence.exception.PrimaryKeyNoValueAssigned;
import ca.uqtr.tp3.persistence.exception.QueryBuilderException;
import ca.uqtr.tp3.persistence.exception.ReflectionException;
import ca.uqtr.tp3.persistence.exception.SQLSyntaxException;
import ca.uqtr.tp3.persistence.metadata.ColumnInfo;
import ca.uqtr.tp3.persistence.metadata.PrimaryKeyInfo;
import ca.uqtr.tp3.persistence.metadata.TableInfo;
import ca.uqtr.tp3.utils.ReflectionUtils;

/**
 * Represent a persistence manager
 *
 *
 */
public class PersistenceManager implements EntityManager {
	private Connection connection;
	private Logger logger;
	
	@Inject
	public PersistenceManager(Connection conn, Logger logger) throws SQLException {
		this.connection = conn;
		this.connection.setSchema("public");
		this.connection.setAutoCommit(true);
		this.logger = logger;	
	}


	@Override
	public <T> List<T> retrieve(Class<T> bean, String sql) throws PersitenceException {

		List<ColumnInfo> columns = ReflectionUtils.getColumns(bean);// On récupere les colonnes


		List<JoinInfo> joins = ReflectionUtils.getRelationsToDisplay(bean, true);// On récupére les jointures

		try {
			// On construit la requête SQL
			Statement stmt;

			stmt = this.connection.createStatement();

			ResultSet result = null;

			result = stmt.executeQuery(sql);

			List<T> beans = new ArrayList<T>();

			while (result.next()) {
				T beanInstance = bean.newInstance();

				// 1. On parcours chaque colonne trouvée dans le bean
				for (ColumnInfo colInfo : columns) {
					// 2. On récupére la valeur pour la colonne à mapper
					Object value = result.getObject(colInfo.getName());

					if (value != null) {
						// 3. On récupére le l'attribut
						Field f = (Field) colInfo.getAnnotatedElement();
						// 4. On s'assure que l'attribut soit accessible
						if (!f.isAccessible()) {
							f.setAccessible(true);
						}
						f.set(beanInstance, value);
					}
				}

				// Récupérer les inner beans s'il y en a
				for (JoinInfo joinInfo : joins) {
					Class<?> innerType = ReflectionUtils.getGenericInnerClass(joinInfo.getAnnotatedElement());
					Object innerBean = null;
					String query = null;
					String[] select = null, from = null, where = null;

					select = ReflectionUtils.buildSelect(innerType);
					from = new String[] { joinInfo.getTableName() };
					where = new String[] {
							String.format("%s.%s = ?", joinInfo.getTableName(), joinInfo.getForeignKey()) };

					query = ReflectionUtils.buildQuery(select, from, where);

					PreparedStatement innerStmt = connection.prepareStatement(query);

					if (joinInfo.isManyToOne()) {
						innerBean = new ArrayList<Object>();
						PrimaryKeyInfo pk = ReflectionUtils.getPrimaryKeys(bean).stream()
								.filter(p -> p.getName().compareTo(joinInfo.getForeignKey()) == 0).findFirst().get();
						innerStmt.setObject(1, result.getObject(pk.getName()));
					} else {
						innerBean = innerType.newInstance();
						innerStmt.setObject(1, result.getObject(joinInfo.getForeignKey()));
					}

					query = innerStmt.toString();

					if (innerBean instanceof List) {
						innerBean = this.retrieve(innerType, query);
					} else {
						innerBean = this.retrieve(innerType, query).get(0);
					}

					Field f = (Field) joinInfo.getAnnotatedElement();

					if (!f.isAccessible()) {
						f.setAccessible(true);
					}

					f.set(beanInstance, innerBean);
				}

				beans.add(beanInstance);
			}

			result.close();
			stmt.close();

			return beans;
		} catch (InstantiationException e) {
			throw new ReflectionException("Can't instanciate object: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ReflectionException(
					"Illegal access to method/attribute, did you forgot to setAccessible(true): " + e.getMessage());
		} catch (SecurityException e) {
			throw new ReflectionException("Security violation: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Invalid arguments: " + e.getMessage());
		} catch (SQLException e) {
			throw new SQLSyntaxException("You're query have some syntax issue: (" + sql + ") " + e.getMessage());
		} catch (PrimaryKeyColumnNotFoundException e) {
			throw new ReflectionException("No primary key found: " + e.getMessage());
		} catch (QueryBuilderException e1) {
			throw new PersitenceException(e1.getMessage(), e1);
		}
	}
	
	@Transactional
	@Log
	@Override
	public <T> int insert(T bean) throws PersitenceException {
		try {

			TableInfo tableInfo = ReflectionUtils.getTableInfo(bean.getClass());
			
			checkPrimaryKeyAutoGenerationPolicy(bean);
			
			// On récupere les colonnes
			List<ColumnInfo> columns = ReflectionUtils.getColumns(bean.getClass());

			// On récupére les jointures
			List<JoinInfo> joins = ReflectionUtils.getRelations(bean.getClass(), true);
			// On récupére les jointures One To One
			List<JoinInfo> oneToOneJoins = joins.stream().filter(j -> !j.isManyToOne()).collect(Collectors.toList());
			int insertedRow = 0;
			
			// Insert direct association first
			if (oneToOneJoins.size() > 0) {
				
				for (JoinInfo join : oneToOneJoins) {
					logger.debug("Saving One To One {}", join);
					Field associationField = (Field) join.getAnnotatedElement();
					associationField.setAccessible(true);

					// 1. Récupère la valeur de la clé primaire pour l'association
					Object association = associationField.get(bean);
						
					insertedRow += insert(association);
					
					PrimaryKeyInfo referencePkInfo = ReflectionUtils.getPrimaryKeys(association.getClass())
							.stream()
							.filter(p -> p.getName().compareTo(join.getForeignKey()) == 0)
							.findFirst()
							.orElseThrow(ReflectionException::new);
					Field referencePkField = (Field) referencePkInfo.getAnnotatedElement();
					referencePkField.setAccessible(true);
					Object referencePkValue = referencePkField.get(association);
					
					// 2. On assigne le valeur de la clé primaire de l'association à l'attribut représentant la clé
					// étrangère pour cette association.
					// Exemple: 
					// 		Registration 								Course
					// 		@Column(name="coursid")	----(reference)--->	@Column(name="coursid")
					// 		Integer courseId	; (referenceFkField)			Integer courseId; (referencePkValue)
					//		@Join
					//		Course course; 
					Field referenceFkField = (Field) columns.stream()
							.filter(c -> c.getName().compareTo(join.getForeignKey()) == 0).findFirst()
							.orElseThrow(() -> new ForeignKeyNotFoundException(association.getClass().getName() + " primary key <" + referencePkInfo.getName() + "> doesnt't match any foreign key(s) in class " + bean.getClass().getName()))
							.getAnnotatedElement();
					referenceFkField.setAccessible(true);
					referenceFkField.set(bean, referencePkValue);
				}
			}

			// Filtering autogenerated primary key if any
			List<ColumnInfo> colWithoutAutoPk = columns
					.stream()
					.filter(c -> (c.getName().compareTo(tableInfo.getPrimaryKeyName()) == 0 && tableInfo.getPrimaryKeyInfo().isAutoGenerated()) == false)
					.collect(Collectors.toList());
			
			// Build the insert statement raw SQL statement
			// Ex:
			// 	Autogenerated pk = false
			//	INSERT INTO cours (coursid, name, description, sigle) VALUES (?, ?, ?, ?);
			//	Autogenerated pk = true
			//	INSERT INTO cours (name, description, sigle) VALUES (?, ?, ?);
			String insertQuery = ReflectionUtils.buildSQLInsertQuery(colWithoutAutoPk, tableInfo);
			
			logger.info("Build insert query {}", insertQuery);
			PreparedStatement stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			
			int index = 1;
			for(ColumnInfo c : colWithoutAutoPk) {
				Field f = (Field) c.getAnnotatedElement();
				f.setAccessible(true);
				
				stmt.setObject(index, f.get(bean) == null ? "" : f.get(bean));
				
				index++;
			}
			
			logger.info("Execute query {}", stmt.toString());
			insertedRow += stmt.executeUpdate();
			
			ResultSet generatedKey = stmt.getGeneratedKeys();
			
			Object primaryKeyValue;
			Field primaryKeyField = (Field) tableInfo.getPrimaryKeyInfo().getAnnotatedElement();
			primaryKeyField.setAccessible(true);
			
			if(tableInfo.getPrimaryKeyInfo().isAutoGenerated()) {
				
				if(!generatedKey.next()) {
					throw new PersitenceException("Something went wrong, the result set should return the genrated primary key, but it didn't!");
				}
				
				primaryKeyValue = generatedKey.getObject(tableInfo.getPrimaryKeyName());
				primaryKeyField.set(bean, primaryKeyValue);
			}else {
				primaryKeyValue = primaryKeyField.get(bean);
			}
			
			List<JoinInfo> manyToManyJoins = joins.stream().filter(j -> j.isManyToOne()).collect(Collectors.toList());
			for (JoinInfo joinInfo : manyToManyJoins) {
				logger.info("Saving Many To Many {}", joinInfo);
				Class<?> innerClass = ReflectionUtils.getGenericInnerClass(joinInfo.getAnnotatedElement());
				Field f = (Field) joinInfo.getAnnotatedElement();
				f.setAccessible(true);
				List<?> innerBeans = (List<?>) f.get(bean);

				ColumnInfo colFk = ReflectionUtils.getColumns(innerClass).stream()
						.filter(c -> c.getName().compareTo(joinInfo.getForeignKey()) == 0)
						.findFirst().orElseThrow(() -> {
							try {
								return new ForeignKeyNotFoundException(bean.getClass(), innerClass);
							} catch (PrimaryKeyColumnNotFoundException e) {
								throw new RuntimeException(e);
							}
						});
				Field fkField = (Field) colFk.getAnnotatedElement();
				fkField.setAccessible(true);

				for (Object innerBean : innerBeans) {
					fkField.set(innerBean, primaryKeyValue);
				}
				insertedRow += bulkInsert(innerBeans);
			}
			
			stmt.close();

			return (int)primaryKeyValue;
		} catch (RuntimeException e) {
			throw new PersitenceException(e);
		} catch (SQLException e) {
			throw new SQLSyntaxException(e);
		} catch (MissingTableAnnotation e) {
			throw new ReflectionException(e);
		} catch (PrimaryKeyColumnNotFoundException e) {
			throw new ReflectionException(e);
		} catch (NoPrimaryKeyFoundException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		} catch (ForeignKeyNotFoundException e) {
			throw new ReflectionException(e);
		} catch (PrimaryKeyNoValueAssigned e) {
			throw new PersitenceException(e);
		}
	}

	/**
	 * Check if the class primary key generation policy 
	 * @param bean
	 * @throws PrimaryKeyColumnNotFoundException
	 * @throws IllegalAccessException
	 * @throws PrimaryKeyNoValueAssigned
	 */
	private <T> void checkPrimaryKeyAutoGenerationPolicy(T bean)
			throws PrimaryKeyColumnNotFoundException, IllegalAccessException, PrimaryKeyNoValueAssigned {
		PrimaryKeyInfo pkInfo;
		pkInfo = ReflectionUtils.getPrimaryKeys(bean.getClass()).get(0);
		Field pkField = (Field) pkInfo.getAnnotatedElement();
		pkField.setAccessible(true);
		Object pkValue = pkField.get(bean);
		
		if(pkValue == null) {
			if(!pkInfo.isAutoGenerated())
				throw new PrimaryKeyNoValueAssigned("checkPrimaryKeyAutoGenerationPolicy(): You have declared a primary key whith no auto generation support, You must supply manually a value to the field "
						+ pkField.getName() + " in " + bean.getClass().getName() + " to properly insert the object.");
		}
	}

	@Transactional
	@Log
	@Override
	public <T> int bulkInsert(List<T> beans) throws PersitenceException {
		return beans.stream().map(b -> {
			try {
				return insert(b);
			} catch (PersitenceException e) {
				throw new RuntimeException(e);
			}
		}).mapToInt(Integer::intValue).sum();
	}
}
