# Compiler-Design

Project Description: The goal of the project is to implement a compiler for a custom small programming langauge. The compiler is written in Java. The target language is java byte code.

ASM byte code framework is used to help with the code generation. The components of compiler implemented are:

1. Scanner - A series of characters were converted into tokens(smallest meaningful units of the custom programming langauage)
2. Parser - The tokens were grouped together into meaningful statements/declarations of the custom programming language. An abstract syntax tree was returned form the Parser
3. Semantic Analyzer - Type Checks were done to ensure the type compatibility between different terms of the custom porgramming language.  
4. Target Code Generator - The code written in the custom programming language was converted to java bytecide using the ASM tool. 

Different JUnit test cases were developed incrementally to verify the correctness of the components designed. 
