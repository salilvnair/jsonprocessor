package com.github.salilvnair.jsonprocessor.path.builder;

import java.util.ArrayList;
import java.util.List;
/**
 * <b>JsonPathBuilder</b> is used to build the json path expression which can be then passed into the
 *  <br><b>JsonPathUtil</b> to search,set,put,add or delete a node in a JSON string/object.
 * @author Salil V Nair
 *
 */
public class JsonPathBuilder {

  private static final String JSON_PATH_EXPR_SELECT_PLACEHOLDER = "<SELECT>";

  public static final String JSON_PATH_EXPR_DOT =".";

  public static final String JSON_PATH_EXPR_SELECT_ALL ="*";

  public static final String JSON_PATH_EXPR_ARRAYNODE_OPEN = "[";

  public static final String JSON_PATH_EXPR_ARRAYNODE_CLOSE = "]";

  private static final String JSON_PATH_EXPR_WHERE_PLACEHOLDER = "<WHERE>";

  private static final String JSON_PATH_EXPR_IS_PLACEHOLDER = "<IS>";

  private static final String JSON_PATH_EXPR_WHERE_IS_PLACEHOLDER = "(@.<WHERE> == <IS>)";
  
  private static final String JSON_PATH_EXPR_LIST_IS_PLACEHOLDER = "(@ == <IS>)";
  
  private static final String JSON_PATH_EXPR_QUERY_PLACEHOLDER = "?";

  private static final String JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER="==";
  
  private static final String JSON_PATH_EXPR_LESS_THAN_QUERY_PLACEHOLDER="<";
  
  private static final String JSON_PATH_EXPR_LESS_THAN_EQUAL_QUERY_PLACEHOLDER="<=";
  
  private static final String JSON_PATH_EXPR_GREATER_THAN_QUERY_PLACEHOLDER=">";
  
  private static final String JSON_PATH_EXPR_GREATER_THAN_EQUAL_QUERY_PLACEHOLDER=">=";
  
  private static final String JSON_PATH_EXPR_NOT_EQUAL_QUERY_PLACEHOLDER="!=";
  
  private static final String JSON_PATH_EXPR_REGEX_QUERY_PLACEHOLDER="=~";
  
  private static final String JSON_PATH_EXPR_IN_QUERY_PLACEHOLDER="in";
  
  private static final String JSON_PATH_EXPR_NOT_IN_QUERY_PLACEHOLDER="nin";
  
  private static final String JSON_PATH_EXPR_QUERY_PLACEHOLDER_MULTICONDTION_OPEN = "?(";

  private static final String JSON_PATH_EXPR_QUERY_PLACEHOLDER_MULTICONDTION_CLOSE = ")";

  private final StringBuilder jsonPathExpressionString = new StringBuilder("$");
  
  private boolean hasMultipleConditions;
  
  private boolean isArrayNodeOfData;

  private List<String> arrayNodeConditions = new ArrayList<String>(5) {
  	private static final long serialVersionUID = 1L;
  	@Override
  	public String toString(){
  		StringBuilder arrayNodeConditionBuilder = new StringBuilder(JSON_PATH_EXPR_ARRAYNODE_OPEN);
  		if(hasMultipleConditions) {
  			arrayNodeConditionBuilder.append(JSON_PATH_EXPR_QUERY_PLACEHOLDER_MULTICONDTION_OPEN);
  		}
  		else {
  			arrayNodeConditionBuilder.append(JSON_PATH_EXPR_QUERY_PLACEHOLDER);
  		}
  		for(String condition:arrayNodeConditions) {
  			arrayNodeConditionBuilder.append(condition);
  		}
  		if(hasMultipleConditions) {
  			arrayNodeConditionBuilder.append(JSON_PATH_EXPR_QUERY_PLACEHOLDER_MULTICONDTION_CLOSE);
  		}
  		arrayNodeConditionBuilder.append(JSON_PATH_EXPR_ARRAYNODE_CLOSE);
      return arrayNodeConditionBuilder.toString();
    }
  };

  private final StringBuilder selectWhere = new StringBuilder(JSON_PATH_EXPR_WHERE_IS_PLACEHOLDER);

  public JsonPathBuilder objectnode(Object... parameters) {
	Object key = null;
	if (parameters.length != 0) {
	  	key = parameters[0];
	}
  	if(key!=null){
  		jsonPathExpressionString.append(JSON_PATH_EXPR_DOT+key);
  	}
  	else {
  		jsonPathExpressionString.append(JSON_PATH_EXPR_DOT);
  	}
    return this;
  }
  
  public JsonPathBuilder any() {	  
	  return this.objectnode();
  }

  public JsonPathBuilder key(String key) {
  	objectnode(key);
  	return this;
  }

  public JsonPathBuilder arraynode(Object... parameters) {
	Object key = null;
	isArrayNodeOfData = false;
	if (parameters.length != 0) {
	  	key = parameters[0];
	}
  	if(!arrayNodeConditions.isEmpty()) {
  		buildNode();
  	}
  	arrayNodeConditions.clear();
  	if(key!=null) {
  		jsonPathExpressionString.append(JSON_PATH_EXPR_DOT+key+JSON_PATH_EXPR_SELECT_PLACEHOLDER);
  	}
  	else {
  		jsonPathExpressionString.append(JSON_PATH_EXPR_DOT+JSON_PATH_EXPR_SELECT_PLACEHOLDER);
  	}
  	
  	if(parameters.length>1 && parameters[1]!=null) {
  		boolean arrayNodeOfData = (boolean) parameters[1];
  		if(arrayNodeOfData) {
  			isArrayNodeOfData = true;
  		}  		
  	}
  	
  	if(parameters.length>2 && parameters[2]!=null) {
  		boolean arrayNodeInCondtion = (boolean) parameters[1];
  		if(arrayNodeInCondtion) {
  			jsonPathExpressionString.setLength(0);
  			jsonPathExpressionString.append(key+JSON_PATH_EXPR_SELECT_PLACEHOLDER);	
  		}  		
  	}
  	  	
    return this;
  }

  private void buildNode() {
  	if(arrayNodeConditions.isEmpty()) {
  		return;
  	}
  	else if(arrayNodeConditions.size()>1) {
  		hasMultipleConditions = true;
  	}
  	int position = jsonPathExpressionString.lastIndexOf(JSON_PATH_EXPR_SELECT_PLACEHOLDER);
  	jsonPathExpressionString.replace(position, position + JSON_PATH_EXPR_SELECT_PLACEHOLDER.length(), arrayNodeConditions.toString());

	}
  
  public JsonPathBuilder select(Object... parameters) {
  	Object key = null;
  	if (parameters.length != 0) {
  		key = parameters[0];
    	if(JSON_PATH_EXPR_SELECT_ALL.equals(key) || key instanceof Integer) {
       	 int position = jsonPathExpressionString.lastIndexOf(JSON_PATH_EXPR_SELECT_PLACEHOLDER);
       	 jsonPathExpressionString.replace(position, position + JSON_PATH_EXPR_SELECT_PLACEHOLDER.length(), JSON_PATH_EXPR_ARRAYNODE_OPEN+key+JSON_PATH_EXPR_ARRAYNODE_CLOSE+"");
    	}
  	}
	selectWhere.setLength(0);
	if(isArrayNodeOfData) {
		selectWhere.append(JSON_PATH_EXPR_LIST_IS_PLACEHOLDER);
	}
	else {
		selectWhere.append(JSON_PATH_EXPR_WHERE_IS_PLACEHOLDER);
	}
  	return this;
  }
  
  public JsonPathBuilder selectAll() {
	  return select(JSON_PATH_EXPR_SELECT_ALL); 
  }

  public JsonPathBuilder where(Object... parameters) {
	String key = (String) parameters[0];
	boolean reverseCondition = false;
	if(parameters.length>1 && parameters[1]!=null) {
		reverseCondition =  (boolean) parameters[1];
	}
	if(reverseCondition) {
		selectWhere.setLength(0);
		selectWhere.append("(<WHERE> == @.<IS>)");
	}
	int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_WHERE_PLACEHOLDER);
  	selectWhere.replace(position, position + JSON_PATH_EXPR_WHERE_PLACEHOLDER.length(), key);
  	return this;
  }

  public JsonPathBuilder is(Object... parameters) {
	Object key = parameters[0];
	boolean forceNonString = false;
	if(parameters.length>1 && parameters[1]!=null) {
		forceNonString =  (boolean) parameters[1];
	}
	int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_IS_PLACEHOLDER);
	if(forceNonString) {
		selectWhere.replace(position, position + JSON_PATH_EXPR_IS_PLACEHOLDER.length(),key+"");
	}
	else {
	  	if(key instanceof String) {
	  		selectWhere.replace(position, position + JSON_PATH_EXPR_IS_PLACEHOLDER.length(), "\""+key.toString()+"\"");
	  	}
	  	else {
	  		selectWhere.replace(position, position + JSON_PATH_EXPR_IS_PLACEHOLDER.length(),key+"");
	  	}
	}  
  	arrayNodeConditions.add(selectWhere.toString());
  	return this;
  }
    
  public JsonPathBuilder lt(Object...parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_LESS_THAN_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  public JsonPathBuilder lte(Object...parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_LESS_THAN_EQUAL_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  public JsonPathBuilder gt(Object... parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_GREATER_THAN_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  public JsonPathBuilder gte(Object... parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_GREATER_THAN_EQUAL_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  public JsonPathBuilder ne(Object... parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_NOT_EQUAL_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  public JsonPathBuilder regex(Object... parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_REGEX_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  public JsonPathBuilder in(Object... parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_IN_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  public JsonPathBuilder nin(Object... parameters) {
	  Object key = parameters[0];
	  replaceOperationPlaceHolder(JSON_PATH_EXPR_NOT_IN_QUERY_PLACEHOLDER);
	  if(parameters.length>1 && parameters[1]!=null) {
		  return is(key,parameters[1]);
	  }
	  return is(key);
  }
  
  private void replaceOperationPlaceHolder(String queryPlaceHolder) {
	  if(JSON_PATH_EXPR_LESS_THAN_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_LESS_THAN_QUERY_PLACEHOLDER);
	  }
	  else if(JSON_PATH_EXPR_LESS_THAN_EQUAL_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_LESS_THAN_EQUAL_QUERY_PLACEHOLDER);
	  }
	  else if(JSON_PATH_EXPR_GREATER_THAN_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_GREATER_THAN_QUERY_PLACEHOLDER);
	  }
	  else if(JSON_PATH_EXPR_GREATER_THAN_EQUAL_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_GREATER_THAN_EQUAL_QUERY_PLACEHOLDER);
	  }
	  else if(JSON_PATH_EXPR_NOT_EQUAL_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_NOT_EQUAL_QUERY_PLACEHOLDER);
	  }
	  else if(JSON_PATH_EXPR_REGEX_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_REGEX_QUERY_PLACEHOLDER);
	  }
	  else if(JSON_PATH_EXPR_IN_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_IN_QUERY_PLACEHOLDER);
	  }
	  else if(JSON_PATH_EXPR_NOT_IN_QUERY_PLACEHOLDER.equals(queryPlaceHolder)) {
		  int position = selectWhere.lastIndexOf(JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER);
		  selectWhere.replace(position, position + JSON_PATH_EXPR_EQUAL_QUERY_PLACEHOLDER.length(),JSON_PATH_EXPR_NOT_IN_QUERY_PLACEHOLDER);
	  }	
  }
   
  public JsonPathBuilder and() {
  	arrayNodeConditions.add(" && ");
	selectWhere.setLength(0);
	if(isArrayNodeOfData) {
		selectWhere.append(JSON_PATH_EXPR_LIST_IS_PLACEHOLDER);
	}
	else {
		selectWhere.append(JSON_PATH_EXPR_WHERE_IS_PLACEHOLDER);
	}
  	return this;
  }

  public JsonPathBuilder or() {
  	arrayNodeConditions.add(" || ");
	selectWhere.setLength(0);
	if(isArrayNodeOfData) {
		selectWhere.append(JSON_PATH_EXPR_LIST_IS_PLACEHOLDER);
	}
	else {
		selectWhere.append(JSON_PATH_EXPR_WHERE_IS_PLACEHOLDER);
	}
  	return this;
  }

  public JsonPathBuilder not() {
  	arrayNodeConditions.add("!");
	selectWhere.setLength(0);
	if(isArrayNodeOfData) {
		selectWhere.append(JSON_PATH_EXPR_LIST_IS_PLACEHOLDER);
	}
	else {
		selectWhere.append(JSON_PATH_EXPR_WHERE_IS_PLACEHOLDER);
	}
  	return this;
  }

  public String build() {
  	buildNode();
  	String evaluatedExpression = this.jsonPathExpressionString.toString();
  	return evaluatedExpression;
  }
  
  public String build(boolean clearString) {
	  	buildNode();
	  	String evaluatedExpression = this.jsonPathExpressionString.toString();
	  	if(clearString) {
	  		clear();
	  	}
	  	return evaluatedExpression;
  }

  public JsonPathBuilder clear() {
  	this.jsonPathExpressionString.setLength(0);
  	this.arrayNodeConditions.clear();
  	this.selectWhere.setLength(0);
  	this.jsonPathExpressionString.append("$");
	return this;
  }

}