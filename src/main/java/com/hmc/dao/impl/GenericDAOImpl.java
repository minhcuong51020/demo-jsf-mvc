package com.hmc.dao.impl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ClassUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hmc.dao.GenericDAO;

public abstract class GenericDAOImpl<T, PK extends Serializable> implements GenericDAO<T, PK> {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	private Class<T> domainClass = (Class<T>) getTypeArguments(GenericDAOImpl.class, this.getClass()).get(0);

	public T findById(PK id) {
		Session session = null;
		Transaction tx = null;
		T object = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			String className = id.getClass().getName();
			switch (className) {
			case "java.lang.String":
				CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
				CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
				Root<T> root = criteriaQuery.from(domainClass);
				criteriaQuery.select(root);
				ClassMetadata classMetadata = sessionFactory.getClassMetadata(domainClass);
				String identifierName = classMetadata.getIdentifierPropertyName();
				Predicate p = criteriaBuilder.like(root.get(identifierName).as(String.class), id.toString());
				criteriaQuery.where(p);
				object = (T) session.createQuery(criteriaQuery).getSingleResult();
				break;
			default:
				object = (T) session.get(domainClass, id);
				break;
			}
			tx.commit();
		} catch (HibernateException e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if(tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		}
		return object;
	}

	public PK save(T object) {
		Session session = null;
		Transaction tx = null;
		PK pk = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			pk = (PK) session.save(object);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return pk;
	}
	
	public void saveOrUpdate(T object) {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(object);
			session.flush();
			tx.commit();
		} catch (HibernateException e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if(tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	public void saveOrUpdate(List<T> objects) {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			int count = 0;
			for (T object : objects) {
				session.saveOrUpdate(object);
				count++;
				if(count % 5 == 0) {
					session.flush();
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	public T merge(T object) {
		Session session = null;
		Transaction tx = null;
		T obj = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			obj = (T) session.merge(object);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return obj;
	}
	
	public void delete(T object) {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.remove(object);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<T> findList() {
		Session session = null;
		Transaction tx = null;
		List<T> objects = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
			Root<T> root = criteriaQuery.from(domainClass);
			criteriaQuery.select(root);
			objects = session.createQuery(criteriaQuery).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return objects;
	}
	
	public List<T> findList(int first, int pageSize, Map<String, Object> filters) {
		Session session = null;
		Transaction tx = null;
		List<T> objects = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
			Root<T> root = criteriaQuery.from(domainClass);
			criteriaQuery.select(root);
			
			// Xử lý filters
			List<Predicate> predicates = setCriteriaFilters(criteriaBuilder, root, filters);
			if(predicates != null) {
				criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
			}
			
			// Xử lý paging
			objects = session.createQuery(criteriaQuery).setFirstResult(first).setMaxResults(pageSize).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return objects;
	}
	
	public List<T> findList(int first, int pageSize, Map<String, Object> filters, Map<String, String> sortOrders) {
		Session session = null;
		Transaction tx = null;
		List<T> objects = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
			Root<T> root = criteriaQuery.from(domainClass);
			criteriaQuery.select(root);
			
			// Xử lý filters
			List<Predicate> predicates = setCriteriaFilters(criteriaBuilder, root, filters);
			if(predicates != null) {
				criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
			}
			// Xử lý orders
			setCriteriaOrders(criteriaBuilder, criteriaQuery, root, sortOrders);
			// Xử lý paging
			objects = session.createQuery(criteriaQuery).setFirstResult(first).setMaxResults(pageSize).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return objects;
	}
	
	public List<T> findListByIsActive(int isActive) {
		Session session = null;
		Transaction tx = null;
		List<T> objects = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);
			Root<T> root = criteriaQuery.from(domainClass);
			criteriaQuery.select(root);
			Predicate p = criteriaBuilder.equal(root.get("isActive").as(Integer.class), isActive);
			criteriaQuery.where(p);
			objects = session.createQuery(criteriaQuery).getResultList();
			tx.commit();
		} catch (HibernateException e) {
			if(tx != null) {
				tx.rollback();
			} 
			e.printStackTrace();
		} catch (Exception ex) {
			if(tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			if(session != null) {
				session.close();
			}
		}
		return objects;
	}
	
	public int count(Map<String, Object> filters) {
		Session session = null;
		Transaction tx = null;
		int count = 0;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<T> root = criteriaQuery.from(domainClass);
			criteriaQuery.select(criteriaBuilder.count(root));
			// Xu ly filter.
			List<Predicate> predicates = setCriteriaFilters(criteriaBuilder, root, filters);
			if(predicates != null) {
				criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
			}
			count = session.createQuery(criteriaQuery).getSingleResult().intValue();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
		return count;
	}
	
	private List<Predicate> setCriteriaFilters(CriteriaBuilder criteriaBuilder, 
									Root<T> root, Map<String, Object> filters) {
		if(filters == null) {
			return null;
		}
		
		Map<String, String> properties = getFields();
		List<Predicate> predicates = new ArrayList<>();
		String type = "";
		String fieldName = "";
		Object fieldValue = "";
		Iterator<String> iterator = filters.keySet().iterator();
		while(iterator.hasNext()) {
			fieldName = iterator.next();
			fieldValue = filters.get(fieldName);
			String[] fields = fieldName.split("\\.");
			type = properties.get(fieldName);
			
			switch (type) {
			case "java.lang.String":
				if(fields.length != 2) {
					Predicate p = criteriaBuilder.like(root.get(fieldName), 
										"%" + fieldValue.toString().toLowerCase() + "%");
					predicates.add(p);
				} else {
					root.join(fields[0]);
					Predicate p = criteriaBuilder.like(root.get(fields[0]).get(fields[1]), "%" + fieldValue.toString().toLowerCase() + "%");
					predicates.add(p);
				}
				break;
			case "java.lang.Integer":
				if(fields.length != 2) {
					Predicate p = criteriaBuilder.equal(root.get(fieldName), Integer.valueOf(fieldValue.toString()));
					predicates.add(p);
				} else {
					root.join(fields[0]);
					Predicate p = criteriaBuilder.equal(root.get(fields[0]).get(fields[1]), Integer.valueOf(fieldValue.toString()));
					predicates.add(p);
				}
				break;
			case "java.lang.Long":
				if(fields.length != 2) {
					Predicate p = criteriaBuilder.equal(root.get(fieldName), Long.valueOf(fieldValue.toString()));
					predicates.add(p);
				} else {
					root.join(fields[0]);
					Predicate p = criteriaBuilder.equal(root.get(fields[0]).get(fields[1]), Long.valueOf(fieldValue.toString()));
					predicates.add(p);
				}
				break;
			case "java.lang.Boolean":
				if(fields.length != 2) {
					Predicate p = criteriaBuilder.equal(root.get(fieldName), fieldValue.equals("1"));
					predicates.add(p);
				} else {
					root.join(fields[0]);
					Predicate p = criteriaBuilder.equal(root.get(fields[0]).get(fields[1]), fieldValue.equals("1"));
					predicates.add(p);
				}
				break;
			case "java.lang.Float":
				if(fields.length != 2) {
					Predicate p = criteriaBuilder.equal(root.get(fieldName), Float.valueOf(fieldValue.toString()));
					predicates.add(p);
				} else {
					root.join(fields[0]);
					Predicate p = criteriaBuilder.equal(root.get(fields[0]).get(fields[1]), Float.valueOf(fieldValue.toString()));
					predicates.add(p);
				}
				break;
			default:
				break;
			}
		}
		return predicates;
	}
	
	private void setCriteriaOrders(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, 
			Root<T> root, Map<String, String> sortOrders) {
		 if(sortOrders == null) {
			 return;
		 }
		 final String _ASC = "ASC";
		 final String _DESC = "DESC";
		 String propertyName = "";
		 String orderType = "";
		 Iterator<String> iterator = sortOrders.keySet().iterator();
		 while(iterator.hasNext()) {
			 propertyName = iterator.next();
			 orderType = sortOrders.get(propertyName);
			 
			 switch (orderType.toUpperCase()) {
			 case _ASC:
				 criteriaQuery.orderBy(criteriaBuilder.asc(root.get(propertyName)));
				break;
			 case _DESC:
				 criteriaQuery.orderBy(criteriaBuilder.desc(root.get(propertyName)));
				 break;
			 default:
				break;
			}
		 }
	}
	
	private Map<String, String> getFields() {
		PropertyDescriptor[] propertyDescriptors;
		Map<String, String> result = new HashMap<>();
		try {
			propertyDescriptors = Introspector.getBeanInfo(domainClass).getPropertyDescriptors();
			String fieldName = "";
			String fieldType = "";
			for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				fieldName = propertyDescriptor.getName();
				fieldType = propertyDescriptor.getPropertyType().getCanonicalName();
				if(ClassUtils.isPrimitiveOrWrapper(Class.forName(fieldType))) {
					result.put(fieldName, fieldType);
				} else if (fieldType.equalsIgnoreCase("java.lang.String")) {
					result.put(fieldName, fieldType);
				} else if(fieldType.equalsIgnoreCase("java.util.Date")) {
					result.put(fieldName, fieldType);
				} else {
					if(!fieldType.equalsIgnoreCase("java.lang.Class")) {
						result = getSubFields(fieldName, fieldType, result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterator<String> it = result.keySet().iterator();
		return result;
	}
	
	private Map<String, String> getSubFields(String fieldName, String fieldType, Map<String, String> result) {
		try {
			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(Class.forName(fieldType))
					.getPropertyDescriptors();
			String subFieldName = "";
			String subFieldType = "";
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				subFieldName = fieldName.concat("." + propertyDescriptor.getName());
				subFieldType = propertyDescriptor.getPropertyType().getCanonicalName();
				if (ClassUtils.isPrimitiveOrWrapper(Class.forName(subFieldType))) {
					result.put(subFieldName, subFieldType);
				} else if (subFieldType.equalsIgnoreCase("java.lang.String")) {
					result.put(subFieldName, subFieldType);
				} else if (subFieldType.equalsIgnoreCase("java.util.Date")) {
					result.put(subFieldName, subFieldType);
				} else {
					if (!subFieldType.equalsIgnoreCase("java.lang.Class")) {
						result = getSubFields(subFieldName, subFieldType, result);
					}
				}
			}
		} catch (IntrospectionException | ClassNotFoundException e) {
		}
		return result;
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable type.
	 * 
	 * @param type the type
	 * @return the underlying class Source:
	 *         https://www.artima.com/weblogs/viewpost.jsp?thread=208860
	 */
	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic base
	 * class.
	 *
	 * @param baseClass  the base class
	 * @param childClass the child class
	 * @return a list of the raw classes for the actual type arguments. Source:
	 *         https://www.artima.com/weblogs/viewpost.jsp?thread=208860
	 */
	public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		Type type = childClass;
		// start walking up the inheritance hierarchy until we hit baseClass
		while (!getClass(type).equals(baseClass)) {
			if (type instanceof Class) {
				// there is no useful information for us in raw types, so just keep going.
				type = ((Class) type).getGenericSuperclass();
			} else {
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class<?> rawType = (Class) parameterizedType.getRawType();

				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
				for (int i = 0; i < actualTypeArguments.length; i++) {
					resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
				}

				if (!rawType.equals(baseClass)) {
					type = rawType.getGenericSuperclass();
				}
			}
		}

		// finally, for each actual type argument provided to baseClass, determine (if
		// possible)
		// the raw class for that type argument.
		Type[] actualTypeArguments;
		if (type instanceof Class) {
			actualTypeArguments = ((Class) type).getTypeParameters();
		} else {
			actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
		}
		List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
		// resolve types by chasing down type variables.
		for (Type baseType : actualTypeArguments) {
			while (resolvedTypes.containsKey(baseType)) {
				baseType = resolvedTypes.get(baseType);
			}
			typeArgumentsAsClasses.add(getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}

}
