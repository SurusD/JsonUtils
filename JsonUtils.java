package org.surus.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public final class JsonUtils {
	private JsonUtils() {

	}

	private static String toString(Object[] array) {
		String toString = "[";
		if (array != null) {
			for (Object value : array) {
				String stringValue = String.valueOf(value);
				boolean isNumber = false;
				boolean isBoolean = false;
				boolean isArray = false;
				boolean isList = false;

				try {
					Number number = (Number) value;
					isNumber = true;
				} catch (Exception e) {
					isNumber = false;
				}

				try {
					boolean b = (boolean) value;
					isBoolean = true;
				} catch (Exception e) {
					isBoolean = false;
				}

				try {
					isList = extendsList(value);
				} catch (Exception e) {
					isList = false;
				}

				try {
					isArray = value != null && value.getClass().isArray();
				} catch (Exception e) {
					isArray = false;
				}
				if (isNumber || isBoolean || isArray) {
					toString += "," + stringValue;
				} else {
					if (isArray) {
						toString += "," + toString((Object[]) value);
					} else {
						toString += ",\"" + stringValue + "\"";
					}
				}
			}
		}
		return toString.replaceFirst(",", "") + "]";
	}

	public static String toJsonArray(Object[] array) {
		return toString(array);
	}

	public static String toJsonString(Class<?> cl, Object instance, boolean isJsonObject) {
		String json = isJsonObject ? "{" : "";
		if (cl != null) {
			for (Class<?> i = cl; i != null; i = null) {
				Field[] fields = i.getDeclaredFields();
				if (fields != null) {
					for (Field field : fields) {
						if (field != null) {
							try {
								field.setAccessible(true);
								String key = field.getName();
								Object value = null;
								if (instance == null) {
									if (Modifier.isStatic(field.getModifiers())) {
										value = field.get(instance);
									}
								} else {
									value = field.get(instance);
								}
								String stringValue = String.valueOf(value);
								boolean isNumber = false;
								boolean isBoolean = false;
								boolean isArray = false;
								boolean isList = false;

								try {
									Number number = (Number) value;
									isNumber = true;
								} catch (Exception e) {
									isNumber = false;
								}

								try {
									boolean b = (boolean) value;
									isBoolean = true;
								} catch (Exception e) {
									isBoolean = false;
								}

								try {
									isList = extendsList(value);
								} catch (Exception e) {
									isList = false;
								}

								try {
									isArray = value != null && value.getClass().isArray();
								} catch (Exception e) {
									isArray = false;
								}

								if (isNumber || isBoolean || isList) {
									json += ",\n\"" + key + "\":" + stringValue;
								} else {
									if (isArray) {
										json += ",\n\"" + key + "\":" + toString((Object[]) value);
									} else {
										json += ",\n\"" + key + "\":\"" + stringValue + "\"";
									}
								}
							} catch (Exception e) {

							}
						}
					}

					Method[] methods = i.getDeclaredMethods();
					if (methods != null) {
						for (Method method : methods) {
							method.setAccessible(true);
							if (method != null) {
								try {
									method.setAccessible(true);
									String key = method.getName();
									Object value = null;
									boolean isSuitableMethod = method.getParameterCount() <= 0;
									if (instance == null) {
										if (Modifier.isStatic(method.getModifiers())) {
											if (isSuitableMethod) {
												value = method.invoke(null);
											}
										}
									} else {
										if (isSuitableMethod) {
											value = method.invoke(instance);
										}
									}
									String stringValue = String.valueOf(value);
									if (value != null) {
										boolean isNumber = false;
										boolean isBoolean = false;
										boolean isArray = false;
										boolean isList = false;

										try {
											Number number = (Number) value;
											isNumber = true;
										} catch (Exception e) {
											isNumber = false;
										}

										try {
											boolean b = (boolean) value;
											isBoolean = true;
										} catch (Exception e) {
											isBoolean = false;
										}

										try {
											isList = extendsList(value);
										} catch (Exception e) {
											isList = false;
										}

										try {
											isArray = value != null && value.getClass().isArray();
										} catch (Exception e) {
											isArray = false;
										}

										if (isNumber || isBoolean || isList) {
											json += ",\n\"" + key + "\":" + stringValue;
										} else {
											if (isArray) {
												json += ",\n\"" + key + "\":" + toString((Object[]) value);
											} else {
												json += ",\n\"" + key + "\":\"" + stringValue + "\"";
											}
										}
									}
								} catch (Exception e) {

								}
							}
						}
						Class<?>[] staticClasses = i.getDeclaredClasses();
						for (Class<?> icl : staticClasses) {
							if (icl != null) {
								String[] split = icl.getName().split("\\$");
								String name = split == null || split.length <= 0 ? icl.getName()
										: split[split.length - 1];
								json += ",\n\"" + name + "\":" + toJsonString(icl, null, true);
							}
						}
					}
				}
			}
		}

		return json.replaceFirst(",", "") + (isJsonObject ? "\n}" : "");
	}

	public static String toJsonString(Class<?> cl) {
		return toJsonString(cl, null);
	}

	public static String toJsonString(Class<?> cl, Object instance) {
		return toJsonString(cl, instance, true);
	}

	public static String toJsonString(Object instance) {
		return toJsonString(instance.getClass(), instance);
	}

	private static boolean extendsList(Object obj) {
		if (obj != null) {
			boolean extendsList = obj instanceof List;
			if (!extendsList) {
				for (Class<?> sclass = obj.getClass(); sclass != null; sclass = sclass.getSuperclass()) {
					if (sclass.isAssignableFrom(List.class)) {
						extendsList = true;
						return true;
					}
				}
			}
			return extendsList;
		}
		return false;
	}
}