package ca.uqtr.tp3.utils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.uqtr.tp3.persistence.annotation.Column;
import ca.uqtr.tp3.persistence.annotation.Ignore;
import ca.uqtr.tp3.persistence.annotation.Join;
import ca.uqtr.tp3.persistence.annotation.PrimaryKey;
import ca.uqtr.tp3.persistence.annotation.Table;
import ca.uqtr.tp3.persistence.exception.ForeignKeyNotFoundException;
import ca.uqtr.tp3.persistence.exception.JoinReflectionException;
import ca.uqtr.tp3.persistence.exception.MissingTableAnnotation;
import ca.uqtr.tp3.persistence.exception.NoPrimaryKeyFoundException;
import ca.uqtr.tp3.persistence.exception.PrimaryKeyColumnNotFoundException;
import ca.uqtr.tp3.persistence.exception.QueryBuilderException;
import ca.uqtr.tp3.persistence.metadata.ColumnInfo;
import ca.uqtr.tp3.persistence.metadata.JoinInfo;
import ca.uqtr.tp3.persistence.metadata.PrimaryKeyInfo;
import ca.uqtr.tp3.persistence.metadata.TableInfo;

public class ReflectionUtils {
	/**
	 * Build SELECT clause by scanning the given class for @Column annotations.
	 * @param bean class to scan column
	 * @return an array of column name
	 */
	public static String[] buildSelect(Class<?> bean) {
		return getColumns(bean)
				.stream()
				.map(c -> {
					try {
						return String.format("%s.%s as %s", getTableInfo(bean).getTableName(), c.getName(), c.getName());
					} catch (Exception e) {
						return "";
					}
				})
				.sorted()
				.toArray(size -> new String[size]);
	}
	
	/**
	 * Build a Many To Many join without a class join table
	 * @param table1
	 * @param table2
	 * @return a list of inner join
	 * @throws MissingTableAnnotation 
	 * @throws PrimaryKeyColumnNotFoundException 
	 * @throws NoPrimaryKeyFoundException
	 */
	public static String[] buildManyToManyJoin(Class<?> table1, Class<?> table2) throws MissingTableAnnotation, PrimaryKeyColumnNotFoundException, NoPrimaryKeyFoundException {
		TableInfo table1Info = getTableInfo(table1);
		JoinInfo joinInfoFromTable1ToTable2 = getRelationForTargetClass(table1, table2);
		
		TableInfo table2Info = getTableInfo(table2);
		JoinInfo joinInfoFromTable2ToTable1 = getRelationForTargetClass(table2, table1);
		
		String[] joins = new String[] {
				String.format("INNER JOIN %s ON %s.%s = %s.%s", 
						joinInfoFromTable1ToTable2.getTableName(),
						joinInfoFromTable1ToTable2.getTableName(),
						joinInfoFromTable1ToTable2.getForeignKey(),
						table1Info.getTableName(), table1Info.getPrimaryKeyName()),
				String.format("INNER JOIN %s ON %s.%s = %s.%s", 
						table2Info.getTableName(),
						table2Info.getTableName(),
						table2Info.getPrimaryKeyName(),
						joinInfoFromTable2ToTable1.getTableName(), joinInfoFromTable2ToTable1.getForeignKey())
		};
		
		return joins;
	}
	
	/**
	 * Build SQL SELECT clause
	 * @param selectClause
	 * @return the SQL SELECT part as String
	 */
	public static String buildSQLSelectClause(String[] selectClause) {
		return String.format("SELECT %s", String.join(", ", selectClause));
	}
	
	/**
	 * Build SQL FROM clause
	 * @param fromClauses
	 * @return the SQL FROM part as String
	 */
	public static String buildSQLFromClause(String[] fromClauses) {
		return String.format("FROM %s", String.join(" ", fromClauses));
	}
	
	/**
	 * Build SQL Join clause from metadata
	 * @param tableInfo
	 * @param joinInfo
	 * @return the SQL inner join as String
	 */
	public static String buildSQLJoinClause(TableInfo tableInfo, JoinInfo joinInfo) {
		return String.format("INNER JOIN %s ON %s.%s = %s.%s",
				tableInfo.getTableName(), tableInfo.getTableName(), 
				tableInfo.getPrimaryKeyName(),
				joinInfo.getTableName(),
				joinInfo.getForeignKey());
	}
	
	/**
	 * Build SQL Join clause from metadata
	 * @param joinInfo
	 * @return the SQL inner join as String
	 * @throws PrimaryKeyColumnNotFoundException 
	 */
	public static String buildSQLJoinClause(JoinInfo joinInfo) throws PrimaryKeyColumnNotFoundException {
		return null;
	}
	
	/**
	 * Build Joins SQL statements
	 * @param joins
	 * @return 
	 */
	public static String buildSQLJoinClause(String[] joins) {
		return String.join(" ", joins);
	}
	
	/**
	 * Build the WHERE SQl statement
	 * @param whereClauses 
	 * @return
	 */
	public static String buildSQLWhereClause(String[] whereClauses) {
		if(whereClauses.length == 1) {
			return String.format("WHERE %s", whereClauses[0]);
		}
		
		List<String> where = new ArrayList<String>();
		where.add(String.format("WHERE %s", whereClauses[0]));
		
		for(int i=1;i<whereClauses.length;i++) {
			where.add(String.format("AND %s", whereClauses[i]));
		}
		
		return String.join(" ", where);
	}
	
	/**
	 * Build a SQL query
	 * 
	 * @param selectClause 
	 * @param fromClause
	 * 
	 * @return the generated SQL statement
	 * @throws Exception 
	 */
	public static String buildQuery(String[] selectClause, String[] fromClause) throws QueryBuilderException {
		return buildQuery(selectClause, fromClause, null, null);
	}
	
	/**
	 * Build a SQL query
	 * 
	 * @param selectClause 
	 * @param fromClause
	 * @param whereClause
	 * 
	 * @return the generated SQL statement
	 * @throws Exception 
	 */
	public static String buildQuery(String[] selectClause, String[] fromClause, String[] whereClause) throws QueryBuilderException {
		return buildQuery(selectClause, fromClause, null, whereClause);
	}
	
	/**
	 * Build a SQL query
	 * 
	 * @param selectClause 
	 * @param fromClause
	 * @param joinClause
	 * @param whereClause
	 * @return the generated SQL statement
	 * @throws Exception 
	 */
	public static String buildQuery(String[] selectClause, String[] fromClause, String[] joinClause, String[] whereClause) throws QueryBuilderException {
		List<String> query = new ArrayList<String>();
		
		if(selectClause == null) {
			throw new QueryBuilderException("You must specify a 'select' clause!");
		} 
		
		query.add(buildSQLSelectClause(selectClause).trim());
		
		if(fromClause == null) {
			throw new QueryBuilderException("You must specify a 'from' clause!");
		}
		
		query.add(buildSQLFromClause(fromClause).trim());
		
		if(joinClause != null) {
			query.add(buildSQLJoinClause(joinClause).trim());
		}
		
		if(whereClause !=null) {
			query.add(buildSQLWhereClause(whereClause).trim());
		}
		
		return String.join(" ", query).trim() + ";";
	}
	
	public static String buildSQLInsertQuery(List<ColumnInfo> columns, TableInfo tableInfo) {
		return String.format("INSERT INTO %s (%s) VALUES (%s);", 
				tableInfo.getTableName(),
				columns.stream().map(c->c.getName()).collect(Collectors.joining(", ")),
				columns.stream().map(c-> "?").collect(Collectors.joining(", "))
				);
	}
	
	/**
	 * Get the generic type of a globing class
	 * @param annotatedElement
	 * 
	 * @return the generic class.
	 */
	public static Class<?> getGenericInnerClass(AnnotatedElement annotatedElement) {
		if(annotatedElement instanceof Field) {
			Field f = (Field) annotatedElement;
			try {
				return (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
			} catch (ClassCastException e) {
				
				return f.getType();
			}
		} else if(annotatedElement instanceof Method) {
			Method m = (Method) annotatedElement;
			return (Class<?>) ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];	
		}
		return null;
	}
	
	/**
	 * Get the setter equivalent of a field or method, the class must follow Java convention regarding getters/setters
	 * For Field will append set + the capitalize version of the field name
	 * Ex: firstame -> setFirstname();
	 * For method: will replace "get" by "set" the method name
	 * Ex: getFirstname() -> setFirstname()
	 * @param annotatedElement
	 * @return the setter methode name
	 */
	public static String getSetter(AnnotatedElement annotatedElement) {
		if(annotatedElement instanceof Method){
			Method m = (Method) annotatedElement;
			return m.getName().replaceFirst("get", "set");
		}else if (annotatedElement instanceof Field) {
			Field f = (Field) annotatedElement;
			return "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
		}
		return null;
	}

	/**
	 * Get a class table info metadata
	 * @param bean
	 * @return
	 * @throws MissingTableAnnotation 
	 * @throws PrimaryKeyColumnNotFoundException 
	 * @throws NoPrimaryKeyFoundException 
	 */
	public static TableInfo getTableInfo(Class<?> bean) throws MissingTableAnnotation, PrimaryKeyColumnNotFoundException, NoPrimaryKeyFoundException {
		return new TableInfo(bean);
	}
	
	/**
	 * Get all field annotated with @Columns metadatas for the given class
	 * 
	 * @param bean class to get columns metadatas
	 * @return
	 */
	public static List<ColumnInfo> getColumns(Class<?> bean) {
		return Arrays.asList(bean.getDeclaredFields()).stream()
		.filter(field -> field.isAnnotationPresent(Column.class))
		.filter(f->hasIgnoreAnnotation(f) == false)
		.sorted((f1, f2) -> f1.getName().compareTo(f2.getName()))
		.map(field -> {	
			return new ColumnInfo(field);
		}).collect(Collectors.toList());
	}
	
	/**
	 * Check if a field or method has an Ignore annotation
	 * @param el
	 * @return
	 */
	private static boolean hasIgnoreAnnotation(AnnotatedElement el) {
		return el.isAnnotationPresent(Ignore.class);
	}
	
	/**
	 * Get the join relation corresponding to the target class in from class 
	 * @param from
	 * @param target
	 * @return
	 */
	public static JoinInfo getRelationForTargetClass(Class<?> from, Class<?> target) {
		List<JoinInfo> rels = getRelations(from, false);
		return rels.stream()
			.filter(r->{
			if(r.getAnnotatedElement() instanceof Method) {
				Method m = (Method) r.getAnnotatedElement();
				Class<?> genericType = (Class<?>) ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
				return genericType.equals(target);
			}else if(r.getAnnotatedElement() instanceof Field) {
				Field f = (Field) r.getAnnotatedElement();
				Class<?> genericType = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
				return genericType.equals(target);
			} else { 
				return false; 
			}
		}).findFirst().orElse(null);
	}
	
	/**
	 * Get all Join metadata from a class
	 * @param beans
	 * @param excludeIgnore if true all @Ignore annotated element will be excluded
	 * @return
	 */
	public static List<JoinInfo> getRelations(Class<?> beans, boolean excludeIgnore) {
		List<JoinInfo> relations = new ArrayList<JoinInfo>();

		Arrays.asList(beans.getDeclaredFields()).stream()
		.filter(field -> {
                    if(hasIgnoreAnnotation(field) == true && (field.getAnnotation(Ignore.class).operation().equals("insert")||field.getAnnotation(Ignore.class).operation().equals("display")))
			{
				return false;
			}
			if(!excludeIgnore) {
				return true;
			}
		
			return hasIgnoreAnnotation(field) == false;
		})
		.filter(method -> method.isAnnotationPresent(Join.class))
				.forEach(field -> {
					try {
						relations.add(new JoinInfo(field));
					} catch (JoinReflectionException | PrimaryKeyColumnNotFoundException | ForeignKeyNotFoundException
							| MissingTableAnnotation | NoPrimaryKeyFoundException e) {
						throw new RuntimeException(e);
					}
				});

		return relations;
	}
	
        
        	public static List<JoinInfo> getRelationsToDisplay(Class<?> beans, boolean excludeIgnore) {
		List<JoinInfo> relations = new ArrayList<JoinInfo>();

		Arrays.asList(beans.getDeclaredFields()).stream()
		.filter(field -> {
                    if(hasIgnoreAnnotation(field) == true && field.getAnnotation(Ignore.class).operation().equals("display"))
			{
				return false;
			}else{
                        if(hasIgnoreAnnotation(field) == true && field.getAnnotation(Ignore.class).operation().equals("insert"))
                        return true;
                    }
		
			return hasIgnoreAnnotation(field) == false;
		})
		.filter(method -> method.isAnnotationPresent(Join.class))
				.forEach(field -> {
					try {
						relations.add(new JoinInfo(field));
					} catch (JoinReflectionException | PrimaryKeyColumnNotFoundException | ForeignKeyNotFoundException
							| MissingTableAnnotation | NoPrimaryKeyFoundException e) {
						throw new RuntimeException(e);
					}
				});

		return relations;
	}
	/**
	 * Get the underlaying value of a field or method
	 * 
	 * @param instance
	 * @param annotatedElement
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object getValue(Object instance, AnnotatedElement annotatedElement) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(annotatedElement instanceof Method) {
			Method m = (Method) annotatedElement;
			return m.invoke(instance);
		}else if(annotatedElement instanceof Field) {
			Field f = (Field) annotatedElement;
			if (f.getModifiers() == Modifier.PUBLIC) {
				return f.get(instance);
			}else {				
				String getter = "get" + f.getName().substring(0,1).toUpperCase()+f.getName().substring(1);
				return instance.getClass().getMethod(getter).invoke(instance);
			}
		}else {
			return null;
		}
	}
	
	/**
	 * Get the field or method representing a primary key
	 * @param bean
	 * @return
	 * @throws MissingTableAnnotation 
	 * @throws PrimaryKeyColumnNotFoundException 
	 * @throws NoPrimaryKeyFoundException 
	 */
	public static AnnotatedElement getPrimaryKeyElement(Class<?> bean) throws MissingTableAnnotation, PrimaryKeyColumnNotFoundException, NoPrimaryKeyFoundException {
		TableInfo info = getTableInfo(bean);
		return getColumns(bean).stream()
				.filter(c -> c.getName().equals(info.getPrimaryKeyName()))
				.map(c -> c.getAnnotatedElement())
				.findFirst().orElse(null);
	}
	
	public static Class<?> getType(AnnotatedElement el) {
		if(el instanceof Field) {
			return ((Field) el).getType();
		}else if(el instanceof Method) {
			return ((Method) el).getReturnType();
		}
		return null;
	}
	
	public static Class<?> getEnglobingClass(AnnotatedElement el){
		if(el instanceof Field) {
			return ((Field) el).getDeclaringClass();
		}else if(el instanceof Method) {
			return ((Method) el).getDeclaringClass();
		}
		
		return null;
	}

	public static String getAnnotatedElementName(AnnotatedElement el) {
		if(el instanceof Field) {
			return ((Field) el).getName();
		}else if(el instanceof Method) {
			return ((Method) el).getName();
		}
		
		return "<Unknown>";
	}

	public static List<PrimaryKeyInfo> getPrimaryKeys(Class<?> bean) throws PrimaryKeyColumnNotFoundException {
		List<PrimaryKeyInfo> pksInfo = new ArrayList<PrimaryKeyInfo>();
		
		if(bean.isAnnotationPresent(Table.class)) {
			Table tableAnnotation = bean.getAnnotation(Table.class);
			String pkFromTable = tableAnnotation.pk();
			if(!pkFromTable.isEmpty()) {
				ColumnInfo pkColumnInfoFromTable = getColumns(bean)
						.stream()
						.filter(c -> c.getName().compareTo(pkFromTable) == 0)
						.findFirst().orElseThrow(() -> new PrimaryKeyColumnNotFoundException(pkFromTable, bean));
				
				PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(pkColumnInfoFromTable.getAnnotatedElement());
				pkInfo.setAutoGenerated(tableAnnotation.autoGenerated());
				pksInfo.add(pkInfo);
			}
		}
		
		pksInfo.addAll(Arrays.asList(bean.getDeclaredFields())
				.stream()
				.filter(f->f.isAnnotationPresent(PrimaryKey.class))
				.map(f-> {
					PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(f);
					pkInfo.setAutoGenerated(f.getAnnotation(PrimaryKey.class).autoGenerated());
					return pkInfo;
				})
				.collect(Collectors.toList()));
		
		return pksInfo;
	}

	public static String[] buildJoins(Class<?> bean) {
		List<JoinInfo> joins = getRelations(bean, true);		
		return joins.stream()
				.map(j -> {
					try {
						return buildSQLJoinClause(j);
					} catch (PrimaryKeyColumnNotFoundException e) {
						throw new RuntimeException(e);
					}
				})
				.toArray(size -> new String[size]);
	}
}
