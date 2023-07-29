package redempt.redlib.nms;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wraps any Object and provides easy access to reflection methods
 * @author Redempt
 */
public class NMSObject {

	private Object obj;
	
	/**
	 * Constructs an NMSObject with the object it should wrap
	 * @param obj The object to wrap
	 */
	public NMSObject(Object obj) {
		this.obj = obj;
	}
	
	/**
	 * @return The wrapped object
	 */
	public Object getObject() {
		return obj;
	}
	
	/**
	 * @return The name of the class of the wrapped object
	 */
	public String getTypeName() {
		return obj.getClass().getSimpleName();
	}
	
	/**
	 * @return A wrapped NMSClass of the class of the wrapped object
	 */
	public NMSClass getType() {
		return new NMSClass(obj.getClass());
	}
	
	/**
	 * @return Whether this NMSObject is wrapping null
	 */
	public boolean isNull() {
		return obj == null;
	}
	
	/**
	 * Calls a method on the wrapped object
	 * @param name The name of the method
	 * @param args The arguments to pass to the method
	 * @param supers The number of superclasses to move up before getting the declared method
	 * @return An NMSObject which is the returned value from the method
	 */
	public NMSObject callMethod(int supers, String name, Object... args) {
		try {
			Method method = NMSHelper.getMethod(getSuperclass(obj.getClass(), supers), name, NMSHelper.getArgTypes(args));
			return new NMSObject(method.invoke(obj, args));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Calls a method on the wrapped object
	 * @param name The name of the method
	 * @param args The arguments to pass to the method
	 * @return An NMSObject which is the returned value from the method
	 */
	public NMSObject callMethod(String name, Object... args) {
		return callMethod(0, name, args);
	}
	
	/**
	 * Gets the value stored in a field in the wrapped object
	 * @param name The name of the field
	 * @param supers The number of superclasses to move up before getting the declared field
	 * @return A wrapped NMSObject with the value of the field
	 */
	public NMSObject getField(int supers, String name) {
		try {
			Field field = getSuperclass(obj.getClass(), supers).getDeclaredField(name);
			field.setAccessible(true);
			return new NMSObject(field.get(obj));
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Gets the value stored in a field in the wrapped object
	 * @param name The name of the field
	 * @return A wrapped NMSObject with the value of the field
	 */
	public NMSObject getField(String name) {
		return getField(0, name);
	}
	
	/**
	 * Sets the value stored in a field in the wrapped object
	 * @param name The name of the field
	 * @param supers The number of superclasses to move up before getting the declared field
	 * @param obj The object to set. Will be unwrapped if it is an NMSObject.
	 */
	public void setField(int supers, String name, Object obj) {
		if (obj instanceof NMSObject) {
			obj = ((NMSObject) obj).getObject();
		}
		try {
			Field field = getSuperclass(this.obj.getClass(), supers).getDeclaredField(name);
			field.setAccessible(true);
			field.set(this.obj, obj);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Sets the value stored in a field in the wrapped object
	 * @param name The name of the field
	 * @param obj The object to set. Will be unwrapped if it is an NMSObject.
	 */
	public void setField(String name, Object obj) {
		setField(0, name, obj);
	}
	
	private Class<?> getSuperclass(Class<?> clazz, int levels) {
		for (int i = 0; i < levels; i++) {
			clazz = clazz.getSuperclass();
		}
		return clazz;
	}
	
}
