/**
 * This code was developed for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2020.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2020 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2020
 *
 */

package cop5556fa20;

import java.awt.Dimension;
import java.util.List;
import cop5556fa20.Scanner.Kind;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cop5556fa20.AST.ASTNode;
import cop5556fa20.AST.ASTVisitor;
import java.awt.image.BufferedImage;
import cop5556fa20.AST.Dec;
import cop5556fa20.AST.DecImage;
import cop5556fa20.AST.DecVar;
import cop5556fa20.AST.ExprArg;
import cop5556fa20.AST.ExprBinary;
import cop5556fa20.AST.ExprConditional;
import cop5556fa20.AST.ExprConst;
import cop5556fa20.AST.ExprEmpty;
import cop5556fa20.AST.ExprHash;
import cop5556fa20.AST.ExprIntLit;
import cop5556fa20.AST.ExprPixelConstructor;
import cop5556fa20.AST.ExprPixelSelector;
import cop5556fa20.AST.ExprStringLit;
import cop5556fa20.AST.ExprUnary;
import cop5556fa20.AST.ExprVar;
import cop5556fa20.AST.Expression;
import cop5556fa20.AST.Program;
import cop5556fa20.AST.StatementAssign;
import cop5556fa20.AST.StatementImageIn;
import cop5556fa20.AST.StatementLoop;
import cop5556fa20.AST.StatementOutFile;
import cop5556fa20.AST.StatementOutScreen;
import cop5556fa20.AST.Type;
import cop5556fa20.runtime.BufferedImageUtils;
import cop5556fa20.runtime.DecImageUtils;
import cop5556fa20.runtime.LoggedIO;
import cop5556fa20.runtime.PLPImage;
import cop5556fa20.runtime.PixelOps;

public class CodeGenVisitorComplete implements ASTVisitor, Opcodes {
	
	final String className;
	final boolean isInterface = false;
	ClassWriter cw;
	MethodVisitor mv;
	
	public CodeGenVisitorComplete(String className) {
		super();
		this.className = className;
	}
	
	
	@Override
	public Object visitDecImage(DecImage decImage, Object arg) throws Exception {
		System.out.println("In visitDecImage block of CodeGenComplete file");
		String varName = decImage.name();
		Expression width = decImage.width();
		Expression height = decImage.height();
		Expression source = decImage.source();
		System.out.println(varName+width+height+source);
		//Check what the value of source is. 
		
		FieldVisitor fieldVisitor = cw.visitField(ACC_STATIC, varName, PLPImage.desc, null, null);
		fieldVisitor.visitEnd();
		
		
		if (source==Expression.empty)
		{
			//Form 1 (image a;)
			if (height == Expression.empty && width==Expression.empty )
			{
				mv.visitInsn(ACONST_NULL);//Buffered Image
				mv.visitInsn(ACONST_NULL);// Dimension
				mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);
				mv.visitFieldInsn(PUTSTATIC, className, varName, PLPImage.desc);
			}
			
			//Form 2 (image[w,h] a;)
			else
			{
				width.visit(this,arg);
				height.visit(this, arg);
				mv.visitMethodInsn(INVOKESTATIC,BufferedImageUtils.className,"createBufferedImage",BufferedImageUtils.createBufferedImagesig,isInterface);
				//mv.visitInsn(ACONST_NULL);
				width.visit(this,arg);
				height.visit(this, arg);
				mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createNewDimension", DecImageUtils.createNewDimensionSig,isInterface);
				mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);
				mv.visitFieldInsn(PUTSTATIC, className, varName, PLPImage.desc);
			}
		}
		
		else
		{
			if (source.type() == Type.String)
			{
				System.out.println("RHS is of type String");
//				FieldVisitor fieldVisitor = cw.visitField(ACC_STATIC, varName, "Ljava/lang/String;", null, null);
//				fieldVisitor.visitEnd();
				source.visit(this, arg);
				mv.visitMethodInsn(INVOKESTATIC,BufferedImageUtils.className,"fetchBufferedImage",BufferedImageUtils.fetchImagesig,isInterface);//BufferedImage
				
				//Form 3 (image a <- b; (b is a string))
				if (width==Expression.empty && height == Expression.empty)
				{
					//Dimension
					mv.visitInsn(ACONST_NULL);
				}
				
				// Form 4 image[w,h] a <- b;  (b is a string)
				else
				{
					System.out.println("LHS is an image with dimensions");
					width.visit(this,arg);
					height.visit(this, arg);
					mv.visitMethodInsn(INVOKESTATIC, BufferedImageUtils.className, "resizeBufferedImage", BufferedImageUtils.resizeImagesig,isInterface);
					width.visit(this,arg);
					height.visit(this, arg);
					mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createNewDimension", DecImageUtils.createNewDimensionSig,isInterface);
					
				}
				mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);
				mv.visitFieldInsn(PUTSTATIC, className, varName, PLPImage.desc);
			}
			else if (source.type() == Type.Image)
			{
				if (decImage.op() == Kind.LARROW)
				{
					System.out.println("Assigning image to varibale using larrow");
					source.visit(this, arg);
					
					// Form 5 image a <- b;  (b is an image)
					if (width==Expression.empty && height == Expression.empty)
					{
						
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "getBufferedImageFromPLPImage", DecImageUtils.getBufferedImageFromPLPImageSig, isInterface);
						//mv.visitFieldInsn(GETSTATIC, BufferedImageUtils.BufferedImageClassName,"image", BufferedImageUtils.BufferedImageDesc);
						//mv.visitInsn(ACONST_NULL);
//						mv.visitInsn(ACONST_NULL);
						mv.visitMethodInsn(INVOKESTATIC, BufferedImageUtils.className, "copyBufferedImage", BufferedImageUtils.copyImagesig,isInterface);
						mv.visitInsn(ACONST_NULL);//Dimension
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);
					}
					
					// Form 6 image[w,h] a <- b; (b is an image)
					else
					{
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "getBufferedImageFromPLPImage", DecImageUtils.getBufferedImageFromPLPImageSig, isInterface);
						mv.visitMethodInsn(INVOKESTATIC, BufferedImageUtils.className, "copyBufferedImage", BufferedImageUtils.copyImagesig,isInterface);
						width.visit(this,arg);
						height.visit(this, arg);
						mv.visitMethodInsn(INVOKESTATIC, BufferedImageUtils.className, "resizeBufferedImage", BufferedImageUtils.resizeImagesig,isInterface);
						width.visit(this,arg);
						height.visit(this, arg);
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createNewDimension", DecImageUtils.createNewDimensionSig,isInterface);
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);	
					}
					mv.visitFieldInsn(PUTSTATIC, className, varName, PLPImage.desc);
				}
				else // op is ASSIGN
				{
					source.visit(this, arg);
					
					if (width==Expression.empty && height == Expression.empty)
					{
						//Form 7 image a = b
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "getBufferedImageFromPLPImage", DecImageUtils.getBufferedImageFromPLPImageSig, isInterface);
						mv.visitInsn(ACONST_NULL);
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);	
					}
					
					else
					{
						int line = decImage.first().line();
						int posline = decImage.first().posInLine();
						System.out.println("In here///////////////////////////////////////////////////////.");
//						mv.visitVarInsn(ASTORE, 1);
//						
//						
////						mv.visitInsn(DUP); //duplicat the top value so we only work on the copy
////						mv.visitFieldInsn(GETSTATIC,"java/lang/System", "out", "Ljava/io/PrintStream;");//put System.out to operand stack
////						mv.visitInsn(SWAP); // swap of the top two values of the opestack: value1 value2 => value2 value1
////						mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V",isInterface);
////						
//						mv.visitVarInsn(ALOAD, 1);
						width.visit(this,arg);
						mv.visitLdcInsn(line);
						mv.visitLdcInsn(posline);
						mv.visitMethodInsn(INVOKEVIRTUAL, PLPImage.className, "compareWidth", PLPImage.compareWidthSig, isInterface);
						
						height.visit(this, arg);
						mv.visitLdcInsn(line);
						mv.visitLdcInsn(posline);
						mv.visitMethodInsn(INVOKEVIRTUAL, PLPImage.className, "compareHeight", PLPImage.compareHeigthSig, isInterface);	
						//TODO check this. Might have to change the definition of PLP Image to include height and width directly. 
						
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "getBufferedImageFromPLPImage", DecImageUtils.getBufferedImageFromPLPImageSig, isInterface);
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createNewDimension", DecImageUtils.createNewDimensionSig,isInterface);
						mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);	
					}
					mv.visitFieldInsn(PUTSTATIC, className, varName, PLPImage.desc);
				}
			}
			
		}
		
		System.out.println("Exiting the DecImage block\n");
		return null;
	}
	
	/**
	 * Add a static field to the class for this variable.
	 */
	@Override
	public Object visitDecVar(DecVar decVar, Object arg) throws Exception {
		System.out.println("In visitDecVar of CodeGenComplete file");
		Expression e = decVar.expression();
		String varName = decVar.name();
		Type type = decVar.type();
		String desc;
		desc="Z";
		if (type == Type.String) 
		{
			desc = "Ljava/lang/String;";
		}
		else if(type== Type.Int)
		{
			desc = "I";
			
		}
		FieldVisitor fieldVisitor = cw.visitField(ACC_STATIC, varName, desc, null, null);
		fieldVisitor.visitEnd();

	
		
		if (e != Expression.empty) {
			e.visit(this, type); // generates code to evaluate expression and leave value on top of the stack
			mv.visitFieldInsn(PUTSTATIC, className, varName, desc);
			System.out.println("Finished Declaration"+varName);
		}
		
		else
		{
			if (type==Type.Int)
			{
				mv.visitInsn(ICONST_0);
			}
				
			else if (type==Type.String || type==Type.Image)
			{
				mv.visitInsn(ACONST_NULL);
			}
				
			mv.visitFieldInsn(PUTSTATIC, className, varName, desc);
		}
		return null;
	}
	@Override
	public Object visitExprArg(ExprArg exprArg, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		Expression e = exprArg.e();
		mv.visitVarInsn(ALOAD, 0);
		e.visit(this, arg);
		mv.visitInsn(AALOAD);
		if (exprArg.type() == Type.Int)
		{
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I",false);
		}	
		return null;
	}
	@Override
	public Object visitExprBinary(ExprBinary exprBinary, Object arg) throws Exception {
		Expression e0 = exprBinary.e0();
		Expression e1 = exprBinary.e1();
		
		Kind op = exprBinary.op();
		
		if (e0!=Expression.empty)
			e0.visit(this, arg);
		if (e1!=Expression.empty)
			e1.visit(this, arg);
		
		
		if (op==Kind.OR)
		{
			mv.visitInsn(IOR);
		}
		else if (op==Kind.AND)
		{
			mv.visitInsn(IAND);
		}
		else if (op==Kind.EQ || op==Kind.NEQ)
		{
			Label labelTrue = new Label();
			Label labelFalse = new Label();
			if (op==Kind.EQ)
			{
				if (e0.type() == Type.String)
					mv.visitJumpInsn(IF_ACMPEQ, labelTrue);
				else
					mv.visitJumpInsn(IF_ICMPEQ, labelTrue);
				mv.visitInsn(ICONST_0);
				mv.visitJumpInsn(GOTO,labelFalse);
				mv.visitLabel(labelTrue);
				mv.visitInsn(ICONST_1);
				mv.visitLabel(labelFalse);
			}
			else if (op==Kind.NEQ)
			{
				if (e0.type() == Type.String)
					mv.visitJumpInsn(IF_ACMPNE, labelTrue);
				else
					mv.visitJumpInsn(IF_ICMPNE, labelTrue);
				mv.visitInsn(ICONST_0);
				mv.visitJumpInsn(GOTO,labelFalse);
				mv.visitLabel(labelTrue);
				mv.visitInsn(ICONST_1);
				mv.visitLabel(labelFalse);
			}
		}
		else if (op==Kind.LT || op == Kind.GT || op==Kind.LE || op==Kind.GE)
		{
			Label labelTrue = new Label();
			Label labelFalse = new Label();
			
			if (op==Kind.LT)
			{
				mv.visitJumpInsn(IF_ICMPLT, labelTrue);
				
			}
			else if (op==Kind.GT)
			{
				mv.visitJumpInsn(IF_ICMPGT, labelTrue);

			}
			else if (op==Kind.GE)
			{
				mv.visitJumpInsn(IF_ICMPGE, labelTrue);

			}
			else if (op==Kind.LE)
			{
				mv.visitJumpInsn(IF_ICMPLE, labelTrue);

			}
			mv.visitInsn(ICONST_0);
			mv.visitJumpInsn(GOTO,labelFalse);
			mv.visitLabel(labelTrue);
			mv.visitInsn(ICONST_1);
			mv.visitLabel(labelFalse);
		}
		else if (op==Kind.PLUS || op==Kind.MINUS)
		{
			if (op==Kind.PLUS)
			{
				if (e1.type()==Type.String)
				{
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;",false);
				}
				else if (e1.type()==Type.Int)
				{
					mv.visitInsn(IADD);
				}
			}
			else if (op==Kind.MINUS)
			{
				mv.visitInsn(ISUB);
			}
		}
		else if (op==Kind.STAR || op==Kind.DIV || op==Kind.MOD)
		{
			if (op==Kind.STAR)
			{
				mv.visitInsn(IMUL);
			}
			else if (op==Kind.DIV)
			{
				mv.visitInsn(IDIV);
			}
			else if (op==Kind.MOD)
			{
				mv.visitInsn(IREM);
			}
		}
		
		return null;
	}
	
	@Override
	public Object visitExprConditional(ExprConditional exprConditional, Object arg) throws Exception {
		
//		Expression cond = exprConditional.condition();
//		Expression trueCase = exprConditional.trueCase();
//		Expression falsecase = exprConditional.falseCase();
//		
//		cond.visit(this, arg);
//		
//		Label True = new Label();
//		Label False = new Label();
//		
//		mv.visitJumpInsn(IFEQ, False);
//		
//		trueCase.visit(this, arg);
//		mv.visitJumpInsn(GOTO,True);
//		
//		mv.visitLabel(False);
//		falsecase.visit(this, arg);
//		
//		mv.visitLabel(True);
		
		Label trueLabel = new Label();
		Label endLabel = new Label();		
		exprConditional.condition().visit(this, null);
		System.out.println("The conditional is >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+exprConditional.condition().visit(this, arg));
		
		mv.visitLdcInsn(true);
		mv.visitJumpInsn(IF_ICMPEQ, trueLabel);
		exprConditional.falseCase().visit(this, null);
		mv.visitJumpInsn(GOTO, endLabel);
		mv.visitLabel(trueLabel);
		exprConditional.trueCase().visit(this, null);
		mv.visitLabel(endLabel);
		
		
//		System.out.println("The conditional is >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+exprConditional.condition().visit(this, arg));
//		exprConditional.condition().visit(this, arg);
//		Label t = new Label();
//		Label f = new Label();
//		mv.visitInsn(ICONST_1);
//		mv.visitJumpInsn(IF_ICMPEQ, t);
//		exprConditional.falseCase().visit(this, arg);
//		mv.visitJumpInsn(GOTO, f);
//		mv.visitLabel(t);
//		exprConditional.trueCase().visit(this, arg);
//		mv.visitLabel(f);
		
	return null;
	}
	
	@Override
	public Object visitExprConst(ExprConst exprConst, Object arg) throws Exception {
		// TODO Auto-generated method stub
		mv.visitLdcInsn(exprConst.value());
		return null;
	}
	@Override
	public Object visitExprHash(ExprHash exprHash, Object arg) throws Exception {
		System.out.println("In visitExprHash CodeGenComplete file");
		exprHash.e().visit(this, arg);
		
		
		if(exprHash.attr().equals("blue"))
		{
			//System.out.println("IN this color");
			mv.visitMethodInsn(INVOKESTATIC, PixelOps.className, "getBlue", PixelOps.getBlueSig, false);
		}
		else if(exprHash.attr().equals("red"))
		{
			//System.out.println("In red");
			mv.visitMethodInsn(INVOKESTATIC, PixelOps.className, "getRed", PixelOps.getRedSig, false);
			//System.out.println("Exit method");
		}
		else if(exprHash.attr().equals("green"))
		{
			mv.visitMethodInsn(INVOKESTATIC, PixelOps.className, "getGreen", PixelOps.getGreenSig, false);
		}
		else if(exprHash.attr().equals("width"))
		{
			int line = exprHash.first().line();
			int posLine = exprHash.first().posInLine();
			mv.visitLdcInsn(line);
			mv.visitLdcInsn(posLine);
			System.out.println("Trying to invoke the PLPException");
//			mv.visitMethodInsn(INVOKEVIRTUAL, className, "getWidthThrows", PLPImage.getWidthThrowsSig,isInterface);
			mv.visitMethodInsn(INVOKESTATIC, PLPImage.className, "getWidthThrows", PLPImage.getWidthThrowsSig, isInterface);
		} 
		else if(exprHash.attr().equals("height"))
		{
			int line = exprHash.first().line();
			int posLine = exprHash.first().posInLine();
			mv.visitLdcInsn(line);
			mv.visitLdcInsn(posLine);
			mv.visitMethodInsn(INVOKEVIRTUAL, className, "getHeightThrows", PLPImage.getHeightThrowsSig,isInterface);
		} 
		return null;
	}
	@Override
	public Object visitExprIntLit(ExprIntLit exprIntLit, Object arg) throws Exception {
		mv.visitLdcInsn(exprIntLit.value());
		return null;
	}
	@Override
	public Object visitExprPixelConstructor(ExprPixelConstructor exprPixelConstructor, Object arg) throws Exception {
		exprPixelConstructor.redExpr().visit(this, arg);
		exprPixelConstructor.blueExpr().visit(this, arg);
		exprPixelConstructor.greenExpr().visit(this, arg);
		mv.visitMethodInsn(INVOKESTATIC, PixelOps.className, "makePixel", "PixelOps.makePixelSig",false);
		return null;
	}
	@Override
	public Object visitExprPixelSelector(ExprPixelSelector exprPixelSelector, Object arg) throws Exception {
		
		exprPixelSelector.image().visit(this, arg);
		exprPixelSelector.X().visit(this, arg);
		 exprPixelSelector.Y().visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, PLPImage.className, "selectPixel", PLPImage.selectPixelSig,isInterface);
		return null;
	}
	
	/**
	 * generate code to put the value of the StringLit on the stack.
	 */
	@Override
	public Object visitExprStringLit(ExprStringLit exprStringLit, Object arg) throws Exception {
		mv.visitLdcInsn(exprStringLit.text());
		//System.out.println("Saving the String literal on  the stack");
		return null;
	}
	@Override
	public Object visitExprUnary(ExprUnary exprUnary, Object arg) throws Exception {
		Kind op = exprUnary.op();
		Expression e = exprUnary.e();
		if (op==Kind.PLUS)
		{
			e.visit(this, arg);
		}
		else if (op==Kind.MINUS)
		{
			e.visit(this, arg);
			mv.visitInsn(INEG);
		}
		else if (op==Kind.EXCL)
		{
			e.visit(this,arg);
			Label trueLabel = new Label();
			Label falseLabel = new Label();
			
			mv.visitInsn(ICONST_1);
			mv.visitJumpInsn(IF_ICMPEQ, trueLabel);
			mv.visitInsn(ICONST_1);
			mv.visitJumpInsn(GOTO,falseLabel);
			mv.visitLabel(trueLabel);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(falseLabel);
		}
		return null;
	}
	@Override
	public Object visitExprVar(ExprVar exprVar, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Type t = exprVar.type();
		String name = exprVar.name();
		
		
		if (t==Type.Int)
		{
			if (name.equals("X"))
			{
				mv.visitVarInsn(ILOAD, 1);
			}
			else if (name.equals("Y"))
			{
				mv.visitVarInsn(ILOAD, 2);
			}
			else
			{
				mv.visitFieldInsn(GETSTATIC, className, name, "I");
			}
		}
		else if (t==Type.String)
		{
			mv.visitFieldInsn(GETSTATIC, className, name, "Ljava/lang/String;");
		}
			
		else if (t==Type.Image)
		{
			mv.visitFieldInsn(GETSTATIC, className, name, PLPImage.desc);
		}
		
		return null;
	}
	
	
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		//cw = new ClassWriter(0); //If the call to methodVisitor.visitMaxs crashes, it
		// is
		// sometime helpful to
		// temporarily run it without COMPUTE_FRAMES. You
		// won't get a completely correct classfile, but
		// you will be able to see the code that was
		// generated.

		// String sourceFileName = className; //TODO Temporary solution, FIX THIS
		int version = -65478;
		cw.visit(version, ACC_PUBLIC | ACC_SUPER, className, null, "java/lang/Object", null);
		cw.visitSource(null, null);
		// create main method
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		// initialize
		mv.visitCode();
		// insert label before first instruction
		Label mainStart = new Label();
		mv.visitLabel(mainStart);
		// visit children to add instructions to method
		List<ASTNode> nodes = program.decOrStatement();
		
		for (ASTNode node : nodes) {
			System.out.println("Visiting node "+ node + "to convert to byte code");
			node.visit(this, null);
		}
		// add  required (by the JVM) return statement to main
		mv.visitInsn(RETURN);

		// adds label at end of code
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		// handles parameters and local variables of main. The only local var is args
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);
		// Sets max stack size and number of local vars.
		// Because we use ClassWriter.COMPUTE_FRAMES as a parameter in the constructor,
		// asm will calculate this itself and the parameters are ignored.
		// If you have trouble with failures in this routine, it may be useful
		// to temporarily set the parameter in the ClassWriter constructor to 0.
		// The generated classfile will not pass verification, but you will at least be
		// able to see what instructions it contains.
		mv.visitMaxs(0, 0);

		// finish construction of main method
		mv.visitEnd();

		// finish class construction
		cw.visitEnd();

		// generate classfile as byte array and return
		return cw.toByteArray();

	}

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws Exception {
		Expression e = statementAssign.expression();
		String name = statementAssign.name();
		Type type = statementAssign.dec().type();
		String desc="";
		System.out.println("entering the statement assign visit method");
//		switch(type)
//		{
//			case String -> {
//				desc = "Ljava/lang/String;";
//				break;
//			}
//			
//			case Int -> {
//				desc = "I";
//				break;
//			}
//			case Boolean-> {
//				desc = "Z";
//				break;
//			}
//			default -> {
//				System.out.println("At visitStatementAssign CodeGenComplete");
//				throw new UnsupportedOperationException("not yet implemented");
//			}
//		}
//		if(type!=Type.Image)
//		{
//			e.visit(this,type);
//			if(e!=Expression.empty)
//			{
//				System.out.println("In the assign statement................");
//				mv.visitFieldInsn(PUTSTATIC, className, name, desc);
//			}
//			
//		}
//		else
//		{
//			
//		}
		
		if (type == Type.String) 
		{
			desc = "Ljava/lang/String;";
			e.visit(this, statementAssign.dec().type());
			if (e!=Expression.empty)
			{
				
				mv.visitFieldInsn(PUTSTATIC, className, name, desc);
			}
		}
		else if (type == Type.Int)
		{
			desc = "I";
			e.visit(this, statementAssign.dec().type());
			if (e!=Expression.empty)
			{
				
				mv.visitFieldInsn(PUTSTATIC, className, name, desc);
			}
		}
		else if (type== Type.Image)
		{
			desc = PLPImage.desc;
			e.visit(this, arg);
			mv.visitVarInsn(ASTORE, 1);
			mv.visitVarInsn(ALOAD, 1);
			
			DecImage decImage = (DecImage) statementAssign.dec();
			
			mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "getBufferedImageFromPLPImage", DecImageUtils.getBufferedImageFromPLPImageSig, isInterface);
			
			if (decImage.width()!=Expression.empty && decImage.height()!=Expression.empty)
			{
				System.out.println("Assignment Statement is not working");
//				mv.visitVarInsn(ALOAD, 1);
//				decImage.width().visit(this, arg);
//				int line = decImage.first().line();
//				int posline = decImage.first().posInLine();
//				mv.visitLdcInsn(line);
//				mv.visitLdcInsn(posline);
//				mv.visitMethodInsn(INVOKEVIRTUAL, PLPImage.className, "compareWidth", PLPImage.compareWidthSig, isInterface);	
//				
//				mv.visitVarInsn(ALOAD, 1);
//				decImage.height().visit(this, arg);
//				mv.visitLdcInsn(line);
//				mv.visitLdcInsn(posline);
//				mv.visitMethodInsn(INVOKEVIRTUAL, PLPImage.className, "compareHeight", PLPImage.compareHeightSig, isInterface);	
//				
//				mv.visitMethodInsn(INVOKESTATIC, LoggedIO.className, "createNewDimension", LoggedIO.dimensionSig,isInterface);
			}
			else
				mv.visitInsn(ACONST_NULL);
			
			mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, isInterface);		
			mv.visitFieldInsn(PUTSTATIC, className, name, PLPImage.desc);
			
			
			
		}

		return null;
	}
	@Override
	public Object visitStatementImageIn(StatementImageIn statementImageIn, Object arg) throws Exception {
		String varName = statementImageIn.name();
		Expression source = statementImageIn.source();
		DecImage decImage = (DecImage) statementImageIn.dec();
		
		source.visit(this, arg);
		
		if (source.type() == Type.String)
		{
			mv.visitMethodInsn(INVOKESTATIC,BufferedImageUtils.className,"fetchBufferedImage",BufferedImageUtils.fetchImagesig,isInterface);
		}
		else
		{
			System.out.println("Need a buffered image +++++++++++++++++++++++++++++++++++++++++++++++++++++");
			mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "getBufferedImageFromPLPImage", DecImageUtils.getBufferedImageFromPLPImageSig, isInterface);
			//mv.visitMethodInsn(INVOKEVIRTUAL, BufferedImageUtils.className, "image", BufferedImageUtils.BufferedImageDesc, isInterface);
			System.out.println("Need a buffered image +++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		if (decImage.width()!=Expression.empty && decImage.height()!=Expression.empty)
		{ 
			System.out.println("Need a buffered image +++++++++++++++++++++++++++++++++++++++++++++++++++++");
			decImage.width().visit(this, arg);
			decImage.height().visit(this, arg);
			mv.visitMethodInsn(INVOKESTATIC, BufferedImageUtils.className, "resizeBufferedImage", BufferedImageUtils.resizeImagesig,isInterface); 
			decImage.width().visit(this, arg);
			decImage.height().visit(this, arg);
			mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createNewDimension", DecImageUtils.createNewDimensionSig,isInterface);
		}
		
		else
		{
			mv.visitInsn(ACONST_NULL);
		}
		
		mv.visitMethodInsn(INVOKESTATIC, DecImageUtils.className, "createPLPImage", DecImageUtils.createPLPImageSig, false);
		mv.visitFieldInsn(PUTSTATIC, className, varName, PLPImage.desc);
		return null;
	}
	@Override
	public Object visitStatementLoop(StatementLoop statementLoop, Object arg) throws Exception {
		Expression condition = statementLoop.cond();
		Expression e = statementLoop.e();
		DecImage decImage = (DecImage) statementLoop.dec();
		Label guardLabelouterLoop = new Label();
		Label bodyLabelouterLoop = new Label();
		Label guardLabelinnerLoop = new Label();
		Label bodyLabelinnerLoop = new Label();
		Label falseLabel = new Label();
		
		mv.visitLdcInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 1);
		mv.visitLdcInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 2);
		
		mv.visitJumpInsn(GOTO,guardLabelouterLoop); 
		mv.visitLabel(bodyLabelouterLoop);
		mv.visitJumpInsn(GOTO,guardLabelinnerLoop); 
		mv.visitLabel(bodyLabelinnerLoop);
		if (condition!=Expression.empty)
			condition.visit(this, arg);
		else
			mv.visitLdcInsn(ICONST_0);
		mv.visitLdcInsn(ICONST_0);
		mv.visitJumpInsn(IF_ICMPEQ, falseLabel);
		mv.visitFieldInsn(GETSTATIC, className, statementLoop.name(), PLPImage.desc);
		mv.visitVarInsn(ILOAD, 1); 
		mv.visitVarInsn(ILOAD, 2); 
		e.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, PLPImage.className, "updatePixel", PLPImage.updatePixelSig, isInterface);
		mv.visitLabel(falseLabel);
		mv.visitIincInsn(2, 1);
		mv.visitLabel(guardLabelinnerLoop);
		mv.visitVarInsn(ILOAD, 2);  
		decImage.height().visit(this, arg);
		mv.visitJumpInsn(IF_ICMPLT, bodyLabelinnerLoop);
		mv.visitIincInsn(1, 1);
		mv.visitLabel(guardLabelouterLoop);
		mv.visitVarInsn(ILOAD, 1);
		decImage.width().visit(this, arg);	
		mv.visitJumpInsn(IF_ICMPLT, bodyLabelouterLoop); 
		return null;
		
	
	}
	@Override
	public Object visitExprEmpty(ExprEmpty exprEmpty, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object visitStatementOutFile(StatementOutFile statementOutFile, Object arg) throws Exception {
		String name = statementOutFile.name();
		Expression file = statementOutFile.filename();
		System.out.println("In visit Statement out file /////////////////////////");
		mv.visitFieldInsn(GETSTATIC, className, name, PLPImage.desc);
		file.visit(this, arg);
		mv.visitMethodInsn(INVOKESTATIC, LoggedIO.className, "imageToFile", LoggedIO.imageToFileSig,isInterface);
		
		return null;
	}

	@Override
	public Object visitStatementOutScreen(StatementOutScreen statementOutScreen, Object arg) throws Exception {
		String name = statementOutScreen.name();
		System.out.println("Detected "+ statementOutScreen.dec());
		Dec dec = statementOutScreen.dec();
		Type type = dec.type();
		String desc;
		if(type == Type.String)
		{
			desc = "Ljava/lang/String;";
			mv.visitFieldInsn(GETSTATIC, className, name, desc);
			mv.visitMethodInsn(INVOKESTATIC, LoggedIO.className, "stringToScreen", LoggedIO.stringToScreenSig,
					false);
		}
		else if(type == Type.Int) 
		{
			desc="I";
			mv.visitFieldInsn(GETSTATIC, className, name, desc);
			mv.visitMethodInsn(INVOKESTATIC, LoggedIO.className, "intToScreen", LoggedIO.intToScreenSig,false);
		}
			
		else if(type == Type.Image) 
		{
			desc= "Lcop5556fa20/runtime/PLPImage;";
			mv.visitFieldInsn(GETSTATIC, className, name, desc);
			statementOutScreen.X().visit(this, arg);
			statementOutScreen.Y().visit(this, arg);
			if (statementOutScreen.X() == Expression.empty)
			{
				mv.visitLdcInsn(ICONST_0);
			}
				
			if (statementOutScreen.Y() == Expression.empty)
			{
				mv.visitLdcInsn(ICONST_0);
			}
				
			mv.visitMethodInsn(INVOKESTATIC, LoggedIO.className, "imageToScreen", LoggedIO.imageToScreenSig,false);
		}
		
		else {
			throw new UnsupportedOperationException("not yet implemented");
		}
		
		return null;
		
	}
}
