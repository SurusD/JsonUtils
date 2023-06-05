# JsonUtils
Convert Java classes to JSON
# How to use

```Java
//Convert to json instance and his class
String json = JsonUtils.toJsonString(this);//convert Java fields, methods with 0 args and classes to json
//Convert to json class and his static fields,methods and classes
json = JsonUtils.toJsonString(getClass());//convert Java fields, methods with 0 args and classes to json
//Convert to json class and instance
json = JsonUtils.toJsonString(getClass(), this);//convert Java fields, methods with 0 args and classes to json
//Convert to json but add or remove { and }
json = JsonUtils.toJsonString(getClass(), this, false);//remove leading brackets { and }
```

