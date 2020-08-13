import java.util.*;

public class SymbolTable extends Object {

  private static boolean noDuplicates = true;

  // Need 3 seperate tables - Symbols, types & values
  // Symboltable hashtable mapping of scope -> linkedlist
  Hashtable<String, LinkedList<String>> symbolTable;
  Hashtable<String, String> typeTable;
  Hashtable<String, String> valuesTable;
  Hashtable<String, String> dupsTable;
  ArrayList<String> funcsTable;
  Hashtable<String, LinkedList<String>> varsTable;

  public SymbolTable(){
    this.symbolTable = new Hashtable<>();
    this.typeTable = new Hashtable<>();
    this.valuesTable = new Hashtable<>();
    this.dupsTable = new Hashtable<>();
    this.funcsTable = new ArrayList<String>();
    this.varsTable = new Hashtable<>();

    // add will have scope global so add by default
    symbolTable.put("global", new LinkedList<>());
  }

  
  // setters & getters

  public LinkedList<String> getSymbolTable(String scope){
    return symbolTable.get(scope);
  }

  public String getType(String id, String scope){
     String type = typeTable.get(id+scope);
     if(type != null) 
       return type;
     else {
      // try global 
      type = typeTable.get(id+"global");
      if(type != null) {
        return type;
      }
    }
    return null;
  }

  public String getValue(String id, String scope){
    String value = valuesTable.get(id+scope);
    if(value != null) 
      return value;
    else {
      value = valuesTable.get(id+"global");
      if(value != null) {
        return value;
      }
    }
    return null;
  }

  
  public void getDupsList(){
    if(dupsTable != null){
      Set<String> keys = dupsTable.keySet();
      for(String key: keys){
         System.out.println("[ scope: "+key+" , id: "+dupsTable.get(key)+" ]");
        }
    }
  }

  public void put(String id, String type, String value, String scope){
    // need to check scope
    // if new scope (not in hashtable) then need to create a new entry of a mapping from
    // that scope to it's own linkedlist
    LinkedList<String> linkedlist = this.symbolTable.get(scope);
    // if not empty - meaning that that scope is already existing & add its linked list
    if(linkedlist == null){
        linkedlist = new LinkedList<>();
        linkedlist.add(id);
        symbolTable.put(scope, linkedlist);
    } else {
      // add to top of llist - why is that?
        linkedlist.addFirst(id);
    }
      typeTable.put(id + scope, type);
      valuesTable.put(id + scope, value);
  }

  // gets how parameter there are based on func_id
  public int getParameterCount(String func_id) {
    LinkedList<String> list = symbolTable.get(func_id);
    int count = 0;
    for(String id : list) {
      String value = valuesTable.get(id+func_id);
      if(value.equals("parameter")) {
        count++;
      }
    }
    return count;
    }

  public boolean duplicates(){
    Enumeration e = symbolTable.keys();
    while (e.hasMoreElements()) {
      String current_scope = (String) e.nextElement();
      LinkedList<String> idList = symbolTable.get(current_scope);
      while (idList.size() > 0) {
        String id = idList.pop();
        if(idList.contains(id)){
          noDuplicates = false;
          dupsTable.put(current_scope, id);
        }
      }
    }
    return noDuplicates;
    }

  public void printTable(){
    Enumeration e = symbolTable.keys();
    // could count how many scopes there are to number them also
    while(e.hasMoreElements()) {
      // get id for each scope
      String scope = (String) e.nextElement();
      // System.out.println(scope);
      // TO DO - number the scopes?
      System.out.println();
      System.out.println();
      System.out.println(String.format("%20s %10s %15s", "Symbol Table #", "|", "Scope: "+scope));
      System.out.println(String.format("%s", "------------------------------------------------------------------"));
      System.out.println();
      // get contents associated with that scope
      LinkedList<String> idList = symbolTable.get(scope);
      System.out.println(String.format("%10s %5s %10s %5s %15s", "Id", "|", "Type", "|", "Description"));
      System.out.println(String.format("%s", "------------------------------------------------------------------"));
      for(String id : idList) {
        String type = typeTable.get(id+scope);
        String vals = valuesTable.get(id+scope);
        System.out.println(String.format("%10s %5s %10s %5s %15s", id, "|", type, "|", vals));
        System.out.println(String.format("%s", "------------------------------------------------------------------"));
      }
    }   
  }

  public ArrayList<String> getFuncTable(){
    Enumeration e = symbolTable.keys();
    while(e.hasMoreElements()) {
      try{
        String scope = (String) e.nextElement();
        String val = this.getValue(scope, scope);
        if(val.equals("function")){
          funcsTable.add(scope);
        }
      }catch(NullPointerException ex){
        //continue
      }
    }
    return funcsTable;
  }
}