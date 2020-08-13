import java.util.*;

public class ThreeAddressCodeBuilder implements HannahParserVisitor {
    //keep track of line
    private static int labelCount = 1;

    private static boolean labelOnLine = false;

    // threeAddressCodeGenerator methods

    // add option to allow : or not
    private void printLabel(final String label, boolean isfunc) {
        if(!isfunc){
          System.out.printf("%-10s", label + ":\n");
          labelOnLine = true;
          labelCount++;
        }else{
            System.out.printf("%-10s", label + "\n");
            labelOnLine = true;
            labelCount++; 
        }
    }

    private void printInstruction(final String instruction) {
        if(labelOnLine) {
            System.out.printf("%s\n", instruction);
            labelOnLine = false;
        }
        else{
           System.out.printf("%s\n", instruction);
        }
      }

    //nodes

    public Object visit(SimpleNode node, Object data){
        throw new RuntimeException("Visit SimpleNode");
    }
    public Object visit(program node, Object data){
        int num = node.jjtGetNumChildren();
        for(int i = 0; i < num; i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return data;
    }
    
    public Object visit(main node, Object data){
        printLabel("main",true);
        System.out.println("\t{");
        int num = node.jjtGetNumChildren();
        for(int i = 0; i < num; i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        System.out.println("\t}");
        return data;
    }
    public Object visit(id node, Object data){
        return node.value;
    }
    public Object visit(var_decl node, Object data){
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        String val = (String) node.jjtGetChild(1).jjtAccept(this, data);
        if(node.jjtGetParent().toString().equals("program")) {
          printInstruction("var " + id + " : " + val);
        }
        return node.value;
    }
    public Object visit(const_decl node, Object data){
        String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
        String val = (String) node.jjtGetChild(2).jjtAccept(this, data);
        printInstruction("const " + id + " = " + val);
        return node.value;
    }

    public Object visit(function node, Object data){
        SimpleNode id = (SimpleNode) node.jjtGetChild(1);
        printLabel((String) id.value,true);
        System.out.println("\t{");
        int num = node.jjtGetNumChildren();
        for(int i = 0; i < num; i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        System.out.println("\t}");
        return data;
    }
    public Object visit(return_statement node, Object data){
        System.out.println("\treturn "); 
        return node.value;
    }
    public Object visit(type node, Object data){
        return node.value;
    }

    public Object visit(parameter node, Object data){
        return node.value;
    }
    public Object visit(assign node, Object data){
        return node.value;
    }
    private int getArgs(arg_list node, Object data){
        int count = 0;
        while(node.jjtGetNumChildren() != 0) {
            count++;
            System.out.println("\tparameter " + node.jjtGetChild(0).jjtAccept(this, data));
            node = (arg_list)node.jjtGetChild(1);
        }   
        return count;
    }

    public Object visit(function_call node, Object data){
        int count = getArgs((arg_list) node.jjtGetChild(1), data);
        String child = (String) node.jjtGetChild(0).jjtAccept(this, data);
        String parent = (node.jjtGetParent()).toString();
        if("statement".equals(parent)){
            System.out.println("\t" + child.toString());
        }
        return node.value;
    }
    public Object visit(statement node, Object data){
        return node.value;
    }
    public Object visit(minus_op node, Object data){
        return node.value;
    }
    public Object visit(integer node, Object data){
        return node.value;
    }
    public Object visit(bool node, Object data){
        return node.value;
    }

    public Object visit(function_return node, Object data){
        String parent = (node.jjtGetParent()).toString();
        if("statement".equals(parent)){
           // System.out.println("\t" + function);
           try{
              //get child
              int count = getArgs((arg_list) node.jjtGetChild(1), data);
              String child = (String) node.jjtGetChild(0).jjtAccept(this, data);
           }catch(Exception e){
           }
        }
        //int count = getArgs((arg_list) node.jjtGetChild(1), data);
        return data;
    }
    public Object visit(arith_ops node, Object data){
        return node.value;
    }
    public Object visit(binary_ops node, Object data){
        return node.value;
    }
    public Object visit(comparison node, Object data){
        return node.value;
    }
    public Object visit(equal_op node, Object data){
        return node.value;
    }
    public Object visit(not_equal_op node, Object data){
        return node.value;
    }
    public Object visit(less_than_op node, Object data){
        return node.value;
    }
    public Object visit(less_than_equal_op node, Object data){
        return node.value;
    }
    public Object visit(greater_than_op node, Object data){
        return node.value;
    }
    public Object visit(greater_than_equal_op node, Object data){
        return node.value;
    }
    public Object visit(arg_list node, Object data){
        if(node.jjtGetNumChildren() !=0){
            String child = (String) node.jjtGetChild(0).jjtAccept(this, data);
            System.out.println("\targ " + child);
        }   
        return data;
    }
}