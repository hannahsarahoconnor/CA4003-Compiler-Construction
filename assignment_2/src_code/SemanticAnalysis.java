
import java.util.*;
//import SymbolTable;
public class SemanticAnalysis implements HannahParserVisitor{
  private static String scope = "global";
  private static boolean noDuplicates = true;
  private static boolean noUndeclared = true;
  private static boolean noIncorrectTypes = true;
  private static boolean noWrongArithOps = true;
  private static boolean noWrongBinaryOps = true;
  private static boolean equalParamArgs = true;
  private static boolean varsReadWrite = true;
  private static boolean funcInvokedId = true;
  private static boolean allFuncsCalled;


  private static Hashtable<String, String> unDeclared = new Hashtable<>();
  private static Hashtable<String, String> unCorrectType = new Hashtable<>();
  private static Hashtable<String, String> wrongArithOps = new Hashtable<>();
  private static Hashtable<String, String> wrongBinaryOps = new Hashtable<>();
  private static Hashtable<String, String> wrongParamsArgs = new Hashtable<>();
  private static ArrayList<String> UncalledFuncList = new ArrayList<String>();
  private static ArrayList<String> calledFuncList = new ArrayList<String>();
  private static ArrayList<String> allFuncsList;
  private static ArrayList<String> scopes_vars;
  private static Hashtable<String, String> nonWroteVarsTable = new Hashtable<>();
  private static SymbolTable symbolTable;

  private static void getSymbolTable(final Object data) {
    symbolTable = (SymbolTable) (data);
  }

  @Override
  public Object visit(final program node, final Object data) {
    getSymbolTable(data);
    final int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }

    if (noUndeclared) {
      System.out.println("1. Passed: Is every identifier declared within scope before its is used?");
    } else {
      System.out.println("1. Failed: Is every identifier declared within scope before its is used?");
      System.out.println("Undeclared");
      System.out.println("_________________________________");
      getUndeclaredList();
    }
    System.out.println();

    noDuplicates = symbolTable.duplicates();
    if (noDuplicates) {
      System.out.println("2. Passed: Is no identifier declared more than once in the same scope?");
    } else {
      System.out.println("2. Failed: Is no identifier declared more than once in the same scope?");
      System.out.println("Duplicates");
      System.out.println("_________________________________");
      symbolTable.getDupsList();
    }
    System.out.println();

    if(noIncorrectTypes){
      System.out.println("3. Passed: Is the left-hand side of an assignment a variable of the correct type?");
      //Is the left-hand side of an assignment a variable of the correct type?
    }else{
      System.out.println("3. Failed: Is the left-hand side of an assignment a variable of the correct type?");
      System.out.println("Not correct type");
      System.out.println("_________________________________");
      getUncorrectTypeList();
    }
    System.out.println();

    if(noWrongArithOps){
      System.out.println("4. Passed: Are the arguments of an arithmetic operator the integer variables or integer constants?");
      //Is the left-hand side of an assignment a variable of the correct type?
    }else{
      System.out.println("4. Failed: Are the arguments of an arithmetic operator the integer variables or integer constants?");
      System.out.println("Not type integer");
      System.out.println("_________________________________");
      getWrongArithList();
    }
    System.out.println();

    if(noWrongBinaryOps){
      System.out.println("5. Passed: Are the arguments of a boolean operator boolean variables or boolean constants?");
    }else{
      System.out.println("5. Failed: Are the arguments of a boolean operator boolean variables or boolean constants?");
      System.out.println("Not type boolean");
      System.out.println("_________________________________");
      getWrongBinaryList();
    }
    System.out.println();

    allFuncsCalled = checkUncalledFuncs();
    if(allFuncsCalled){
      System.out.println("6. Passed: Is every function called?");
    }else{
      System.out.println("6. Failed: Is every function called?\n");
      System.out.println("Uncalled functions");
      System.out.println("_________________________________");
      getUncalledFuncList();
    }
    System.out.println();

    if(equalParamArgs){
      System.out.println("7. Passed: Does every function call have the correct number of arguments?");
    }else{
      System.out.println("7. Failed: Does every function call have the correct number of arguments?\n");
      System.out.println("Incorrect functions calls (args)");
      System.out.println("_________________________________");
      getWrongParamsArgs();
    }
    System.out.println();

    if(varsReadWrite){
      System.out.println("8. Passed: Is every variable both written to and read from?");
    }else{
      System.out.println("8. Failed: Is every variable both written to and read from?\n");
      System.out.println("Variables not wrote/read");
      System.out.println("_________________________________");
      getvarsReadWriteList();
    }
    System.out.println();

    if(funcInvokedId){
      System.out.println("9. Passed: Is there a function for every invoked identifier?");
      //Is the left-hand side of an assignment a variable of the correct type?
    }else{
      System.out.println("9. Failed: Is there a function for every invoked identifier?");
    }
    return data;
  }

  private static boolean declaredCheck(final String id, final String scope) {
    final LinkedList<String> list = symbolTable.getSymbolTable(scope);
    final LinkedList<String> global_list = symbolTable.getSymbolTable("global");
    if (list != null) {
      if (!global_list.contains(id) && !list.contains(id)) {
        return false;
      } else {
        return true;
      }
    }
    return true;
  }

  public static void getUndeclaredList() {
    if (unDeclared != null) {
      final Set<String> keys = unDeclared.keySet();
      for (final String key : keys) {
        System.out.println("[ scope: " + key + " , id: " + unDeclared.get(key) + " ]");
      }
    }
  }

  public static void getUncorrectTypeList() {
    if (unCorrectType != null) {
      final Set<String> keys = unCorrectType.keySet();
      for (final String key : keys) {
        System.out.println("[ scope: " + key + " , id: " + unDeclared.get(key) + " ]");
      }
    }
  }

  public static void getWrongArithList() {
    if (wrongArithOps != null) {
      final Set<String> keys = wrongArithOps.keySet();
      for (final String key : keys) {
        System.out.println("[ scope: " + key + " , id: " + wrongArithOps.get(key) + " ]");
      }
    }
  }

  public static void getWrongBinaryList() {
    if (wrongBinaryOps != null) {
      final Set<String> keys = wrongBinaryOps.keySet();
      for (final String key : keys) {
        System.out.println("[ scope: " + key + " , id: " + wrongBinaryOps.get(key) + " ]");
      }
    }
  }

  public static void getWrongParamsArgs() {
    if (wrongParamsArgs != null) {
      final Set<String> keys = wrongParamsArgs.keySet();
      for (final String key : keys) {
        System.out.println("[ function: " + key + " , excepted args: " + wrongParamsArgs.get(key) + " ]");
      }
    }
  }

  public static void getvarsReadWriteList(){
    if (nonWroteVarsTable!= null) {
      final Set<String> keys = nonWroteVarsTable.keySet();
      for (final String key : keys) {
        System.out.println("[ function: " + key + " , variable: " + nonWroteVarsTable.get(key) + " ]");
      }
    }
  }


  public boolean checkUncalledFuncs(){
    allFuncsCalled = true;
    allFuncsList = symbolTable.getFuncTable();
    for(String func : allFuncsList){
      if(!calledFuncList.contains(func)){
        allFuncsCalled = false;
        UncalledFuncList.add(func);
      }
    }
    return allFuncsCalled;
  }

  public static void getUncalledFuncList() {
    // list of created functions ->
    if (UncalledFuncList != null) {
      for (final String func : UncalledFuncList) {
        System.out.println("[ id: " + func + " ]");
      }
    }
  }

  public int getArgs(arg_list node, Object data){
    int count = 0;
    while(node.jjtGetNumChildren() != 0) {
        count++;
        // get next node 
        node = (arg_list) node.jjtGetChild(0);
    }  
    return count;
}

  // Nodes

  @Override
  public Object visit(statement node, Object data) {
    String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
    
    // check if theres an id for invoked function

    try{
    String child1 = (String) node.jjtGetChild(0).toString();
    String child2 = (String) node.jjtGetChild(1).toString();
    String child3 = (String) node.jjtGetChild(2).toString();
  
    if(child1.equals("id") && child2.equals("arg_list") && child3.equals("function_call")){
      funcInvokedId = false;
    }
  }catch(Exception e){
    //
  }
    // Declared Check
    try {
    //   // TODO - Fix
    //   // Some reason my ids are printing as
    //   // id = x
    //   // id = 0
    //   // id y
    //   // id -6
    //   // Adding a work around check to make sure the id isn't a int/float!
      Integer.parseInt(id);
    } catch (final Exception e) {
      if (!declaredCheck(id, scope)) {
        noUndeclared = false;
        unDeclared.put(scope, id);
      }
    }
    // TO DO - Fix 
    // Left Hand side Check
      // Look the id up in the symbol table to get type
      String type = symbolTable.getType(id, scope);
      String value = symbolTable.getValue(id, scope);
      String rhs = node.jjtGetChild(1).toString();

    try{      
      // need to consider when an function is invoked to a id!
        String test = (String) node.jjtGetChild(1).jjtAccept(this, data);
        String test_value = symbolTable.getValue(test, scope);

        //TODO MAKE A FUNCTION FOR THE TWO FUNCTION CASES!!
        if(test_value.equals("function")){
          calledFuncList.add(test);
          int testParametersNumber = symbolTable.getParameterCount(test);
          int testArgumentsNumber = getArgs((arg_list) node.jjtGetChild(1).jjtGetChild(0), data);
          
          if(!(testParametersNumber == testArgumentsNumber)){
            equalParamArgs = false;
            // add the func id & expected
            wrongParamsArgs.put(test, String.valueOf(testParametersNumber));
          }
        }
      // check if invoked function (function return is a child of statement)
        if(value.equals("function")){
          // dd to list 
          calledFuncList.add(id);
        
          // counts the entries of value "parameter" in that functions scope
          int parametersNumber = symbolTable.getParameterCount(id);
          //need to now access the child "arg_list" to examine it's no. args
          //in format
          //statement
          // function_return
          //   arg_list
          // func -> node.jjtGetChild(1) <- so get 1st child of that child
          int argumentsNumber= node.jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren();
          if(!(parametersNumber == argumentsNumber)){
            equalParamArgs = false;
            // add the func id & expected
            wrongParamsArgs.put(id, String.valueOf(parametersNumber));
          }

        }
        if(value.equals("variable")){
        // need to add a check for each type - only 2 in the grammar
          //given_value = node.jjtGetChild(1).jjtAccept(this, data);
          if(type.equals("integer")) {
            // integer
            if(rhs.equals("integer"))
            {
                node.jjtGetChild(1).jjtAccept(this, data);
            }
            else if(rhs.equals("bool")) {
              System.out.println("expected integer got boolean");
            }
            //check that 
          }else if(type.equals("boolean")) {
            if(rhs.equals("bool")){
                node.jjtGetChild(1).jjtAccept(this, data);
            }else if(rhs.equals("integer")){
                System.out.println("expected boolean got integer");
            }   
          }
        }
        try{
        //check if any of the children are arith_ops!
        int numChildren = node.jjtGetNumChildren();
        for (int i = 1; i < numChildren; i++) {
            String before = (String) node.jjtGetChild(i-1).jjtAccept(this, data);
            String childstr = (String) node.jjtGetChild(i).toString();
            String after = (String) node.jjtGetChild(i+1).jjtAccept(this, data);
          if(childstr.equals("arith_ops")){
            //lhs is id
            // get type of next arg
            String type1 = symbolTable.getType(before, scope);
            String type2 = symbolTable.getType(after, scope);
            if(!type1.equals(type2)){
              noWrongArithOps = false;
              // add whichever one doesnt have type integer to set.
              // put scope & id.
              if(type1.equals("integer")){
                // add type 2
                wrongArithOps.put(scope,after);
              }else if(type2.equals("integer")){
                // add type 1
                wrongArithOps.put(scope,before);
              }else{
                wrongArithOps.put(scope,before);
                wrongArithOps.put(scope,after);
              }

          }
          }else if(childstr == "binary_ops"){
            // TODO make this to a function & only pass the rhs & excepted type - same code as arithops
            String type1 = symbolTable.getType(before, scope);
            String type2 = symbolTable.getType(after, scope);
            if(!type1.equals(type2)){
              noWrongBinaryOps = false;
              // add whichever one doesnt have type integer to set.
              // put scope & id.
              if(type.equals("boolean")){
                // add type 2
                wrongBinaryOps.put(scope,after);
              }else if(type2.equals("boolean")){
                // add type 1
                wrongBinaryOps.put(scope,before);
              }else{
                wrongBinaryOps.put(scope,before);
                wrongBinaryOps.put(scope,after);
              }

          }
        }
      }
      }catch(Exception e){
        //
      }
    }catch(Exception e){
       //Ignoring this exception for now
       // TODO invesitage this
       //  if(type.equals(null)){
          //  }
    }
    return data;
  }

  @Override
  public Object visit(final parameter node, final Object data) {
    final int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    return data;
  }

  @Override
  public Object visit(final SimpleNode node, final Object data) {
    throw new RuntimeException("Visit SimpleNode");
  }

  @Override
  public Object visit(final main node, final Object data) {
    // set the scope!!
    this.scope = "main";
    final int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    return data;
  }

  public void isWrote(String scope, String id){
    scopes_vars = new ArrayList<String>();
    LinkedList<String> idList = symbolTable.getSymbolTable(scope);
    for(String s : idList){
      String vals = symbolTable.getValue(s, scope);
      if(vals.equals("variable")){
        scopes_vars.add(s);
      }
      }
    if(!scopes_vars.contains(id)){
      varsReadWrite = false;
      nonWroteVarsTable.put(scope,id);
    } 
  }

  @Override
  public Object visit(final id node, final Object data) {
    String id = (String) node.jjtGetValue();
    String val = symbolTable.getValue(id, scope);
    try{
      if(val.equals("variable")){
        isWrote(scope, id);
      }
   }catch(NullPointerException e){
     //
   }
   return node.value;
  }

  @Override
  public Object visit(final var_decl node, final Object data) {
    return data;
  }

  @Override
  public Object visit(final const_decl node, final Object data) {
    return data;
  }

  @Override
  public Object visit(final function node, final Object data) {
    // set function scope!!
    this.scope = (String) node.jjtGetChild(1).jjtAccept(this, data);
    final int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    return data;
  }

  public Object visit(arith_ops node, Object data){
    return node.value;
  }

  public Object visit(binary_ops node, Object data){
    return node.value;
  }

  @Override
  public Object visit(final type node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final assign node, final Object data) {
    return data;
  }

  @Override
  public Object visit(final minus_op node, final Object data) {
    return data;
  }

  @Override
  public Object visit(final integer node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final bool node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final function_call node, final Object data) {
    return data;
  }

  @Override
  public Object visit(final equal_op node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final not_equal_op node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final less_than_op node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final less_than_equal_op node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final greater_than_op node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final greater_than_equal_op node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final arg_list node, final Object data) {
    node.childrenAccept(this, data);
    return data;
  }

  @Override
  public Object visit(final comparison node, final Object data) {
    node.childrenAccept(this, data);
    return node.value;
  }

  @Override
  public Object visit(final function_return node, final Object data) {
    return node.value;
  }

  @Override
  public Object visit(final return_statement node, final Object data) {
    return data;
  }
}