/**
 * 
 */
package org.jumbune.datavalidation.xml;
/**
 * 
 */

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jumbune.common.beans.XmlElementBean;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSUnionSimpleType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XSWildcard.Any;
import com.sun.xml.xsom.XSWildcard.Other;
import com.sun.xml.xsom.XSWildcard.Union;
import com.sun.xml.xsom.XSXPath;
import com.sun.xml.xsom.impl.Const;
import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
import com.sun.xml.xsom.visitor.XSTermVisitor;
import com.sun.xml.xsom.visitor.XSVisitor;
import com.sun.xml.xsom.visitor.XSWildcardFunction;

/**
 * Generates approximated XML Schema representation from
 * a schema component. This is not intended to be a fully-fledged
 * round-trippable schema writer.
 *
 * <h2>Usage of this class</h2>
 * <ol>
 *  <li>Create a new instance with whatever Writer
 *      you'd like to send the output to.
 *  <li>Call one of the overloaded dump methods.
 *      You can repeat this process as many times as you want.
 * </ol>
 *
 */
public class JumbuneSchemaWriter implements XSVisitor, XSSimpleTypeVisitor {
	
	Map<String,XmlElementBean>  elementsMap ;
	Map<String,String> uriMapping ;
	String rootPrefix = "";
	
    public JumbuneSchemaWriter( Writer _out, Map<String,XmlElementBean>  mapParam,Map<String,String> uriMapping ) {
        this.out=_out;
        this.elementsMap = mapParam ;
        this.uriMapping = uriMapping;
    }

    /** output is sent to this object. */
    private final Writer out;

    /** indentation. */
    private int indent;

    private void println(String s) {
        try {
            for( int i=0; i<indent; i++)    out.write("  ");
            
            String itr = s;
            
            for(String prefix : uriMapping.keySet()){
           	 
           	 String uri = uriMapping.get(prefix);
           	
           	 itr = StringUtils.replace(itr, "{"+uri+"}", prefix+":");
           	 itr = StringUtils.replace(itr, "<"+uri+">", prefix+":");
           	 
            }
            
             itr = StringUtils.replace(itr, XmlDataValidationConstants.SCHEMA, rootPrefix+XmlDataValidationConstants.SCHEMA);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.COMPLEX_TYPE, rootPrefix+XmlDataValidationConstants.COMPLEX_TYPE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.SEQUENCE, rootPrefix+XmlDataValidationConstants.SEQUENCE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.ATT_GROUP, rootPrefix+XmlDataValidationConstants.ATT_GROUP);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.ATTRIBUTE, rootPrefix+XmlDataValidationConstants.ATTRIBUTE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.SIMPLE_TYPE, rootPrefix+XmlDataValidationConstants.SIMPLE_TYPE);
             itr = StringUtils.replace(itr,"<"+XmlDataValidationConstants.LIST, "<"+rootPrefix+XmlDataValidationConstants.LIST);
             itr = StringUtils.replace(itr,"</"+XmlDataValidationConstants.LIST, "</"+rootPrefix+XmlDataValidationConstants.LIST);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.UNION, rootPrefix+XmlDataValidationConstants.UNION);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.RESTRICTION, rootPrefix+XmlDataValidationConstants.RESTRICTION);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.NOTATION, rootPrefix+XmlDataValidationConstants.NOTATION);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.EXTENSION, rootPrefix+XmlDataValidationConstants.EXTENSION);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.REDEFINE, rootPrefix+XmlDataValidationConstants.REDEFINE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.SIMPLE_CONTENT, rootPrefix+XmlDataValidationConstants.SIMPLE_CONTENT);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.COMPLEX_CONTENT,rootPrefix+XmlDataValidationConstants.COMPLEX_CONTENT);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.MIN_LENGTH, rootPrefix+XmlDataValidationConstants.MIN_LENGTH);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.MAX_LENGTH, rootPrefix+XmlDataValidationConstants.MAX_LENGTH);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.ELEMENT, rootPrefix+XmlDataValidationConstants.ELEMENT);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.PATTERN, rootPrefix+XmlDataValidationConstants.PATTERN);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.ENUMERATION, rootPrefix+XmlDataValidationConstants.ENUMERATION);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.MIN_INCLUSIVE, rootPrefix+XmlDataValidationConstants.MIN_INCLUSIVE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.MIN_EXCLUSIVE, rootPrefix+XmlDataValidationConstants.MIN_EXCLUSIVE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.MAX_INCLUSIVE, rootPrefix+XmlDataValidationConstants.MAX_INCLUSIVE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.MAX_EXCLUSIVE, rootPrefix+XmlDataValidationConstants.MAX_EXCLUSIVE);
             itr = StringUtils.replace(itr, "<"+XmlDataValidationConstants.ALL, "<"+rootPrefix+XmlDataValidationConstants.ALL);
             itr = StringUtils.replace(itr, "</"+XmlDataValidationConstants.ALL, "</"+rootPrefix+XmlDataValidationConstants.ALL);
             itr = StringUtils.replace(itr, "<"+XmlDataValidationConstants.GROUP, "<"+rootPrefix+XmlDataValidationConstants.GROUP);
             itr = StringUtils.replace(itr, "</"+XmlDataValidationConstants.GROUP, "</"+rootPrefix+XmlDataValidationConstants.GROUP);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.WHITE_SPACE, rootPrefix+XmlDataValidationConstants.WHITE_SPACE);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.TOTAL_DIGITS, rootPrefix+XmlDataValidationConstants.TOTAL_DIGITS);
             itr = StringUtils.replace(itr, XmlDataValidationConstants.FRACTION_DIGITS, rootPrefix+XmlDataValidationConstants.FRACTION_DIGITS);
             itr = StringUtils.replace(itr, "<"+XmlDataValidationConstants.LENGTH, "<"+rootPrefix+XmlDataValidationConstants.LENGTH);
             itr = StringUtils.replace(itr, "</"+XmlDataValidationConstants.LENGTH, "</"+rootPrefix+XmlDataValidationConstants.LENGTH);
         	
             itr = StringUtils.replace(itr, "{}", "");
            
            
            out.write(itr);
            out.write('\n');
            // flush stream to make the output appear immediately
            out.flush();
        } catch( IOException e ) {
            // ignore IOException.
            hadError = true;
        }
    }
    private void println() { println(""); }

    /** If IOException is encountered, this flag is set to true. */
    private boolean hadError =false;

    /** Flush the stream and check its error state. */
    public boolean checkError() {
        try {
            out.flush();
        } catch( IOException e ) {
            hadError=true;
        }
        return hadError;
    }

    public void visit( XSSchemaSet s ) {
        Iterator<XSSchema> itr =  s.iterateSchema();
        while(itr.hasNext()) {
            schema(itr.next());
            println();
        }
    }

    public void schema( XSSchema s) {
    	
    	if(this.elementsMap.size() != 0){

    	StringBuffer sb = new StringBuffer();
         
         for(String prefix : uriMapping.keySet()){
        	 
        	 String uri = uriMapping.get(prefix);
        	 if(Const.schemaNamespace.equalsIgnoreCase(uri)) rootPrefix = prefix+":";
        	 sb.append("xmlns:"+prefix+"=\""+uri+"\" " );
        	 
         }
         
         if(!s.getTargetNamespace().isEmpty()){
        	 println(MessageFormat.format("<schema {0} targetNamespace=\"{1}\">",sb.toString(),s.getTargetNamespace()));
         }else{
        	 println(MessageFormat.format("<schema {0} >",sb.toString()));
         }
         
          indent++;

          Iterator<XSAttGroupDecl> itrAGD = s.iterateAttGroupDecls();
          while (itrAGD.hasNext()) {
        	  attGroupDecl(itrAGD.next());
          }

          Iterator<XSAttributeDecl> itrAD = s.iterateAttributeDecls();
          while(itrAD.hasNext()) {
        	  attributeDecl(itrAD.next());
          }

          Iterator<XSComplexType> itrCT = s.iterateComplexTypes();
          while(itrCT.hasNext()) {
        	  complexType(itrCT.next());
          }
          
          Iterator<XSElementDecl> itrED = s.iterateElementDecls();
          while(itrED.hasNext()) {
        	  elementDecl(itrED.next());
          } 
          
          Iterator<XSModelGroupDecl> itrMGD = s.iterateModelGroupDecls();
          while(itrMGD.hasNext()) {
        	  modelGroupDecl(itrMGD.next()); 
          }

          Iterator<XSSimpleType> itrST = s.iterateSimpleTypes();
          while(itrST.hasNext()) {
        	  simpleType(itrST.next());
          }

          indent--;
          println("</schema>");
    	}
    	//}

       
    }

    public void attGroupDecl( XSAttGroupDecl decl ) {
        Iterator<?> itr;

        println(MessageFormat.format("<attGroup name=\"{0}\">", decl.getName()));
        indent++;
        
        // TODO: wildcard
        
        itr = decl.iterateAttGroups();
        while(itr.hasNext())
            dumpRef( (XSAttGroupDecl)itr.next() );

        itr = decl.iterateDeclaredAttributeUses();
        while(itr.hasNext())
            attributeUse( (XSAttributeUse)itr.next() );

        indent--;
        println("</attGroup>");
    }

    public void dumpRef( XSAttGroupDecl decl ) {
        println(MessageFormat.format("<attGroup ref=\"'{'{0}'}'{1}\"/>", decl.getTargetNamespace(), decl.getName()));
    }

    public void attributeUse( XSAttributeUse use ) {
        XSAttributeDecl decl = use.getDecl();

        String additionalAtts="";

        if(use.isRequired())
            additionalAtts += " use=\"required\"";
        if(use.getFixedValue()!=null && use.getDecl().getFixedValue()==null)
            additionalAtts += " fixed=\""+use.getFixedValue()+'\"';
        if(use.getDefaultValue()!=null && use.getDecl().getDefaultValue()==null)
            additionalAtts += " default=\""+use.getDefaultValue()+'\"';

        if(decl.isLocal()) {
            // this is anonymous attribute use
            dump(decl,additionalAtts);
        } else {
            // reference to a global one
            println(MessageFormat.format("<attribute ref=\"{0}{1}\"{2}/>",
                decl.getTargetNamespace(), decl.getName(), additionalAtts));
        }
    }

    public void attributeDecl( XSAttributeDecl decl ) {
        dump(decl,"");
    }

    private void dump( XSAttributeDecl decl, String additionalAtts ) {
        XSSimpleType type=decl.getType();

        println(MessageFormat.format("<attribute name=\"{0}\"{1}{2}{3}{4}{5}>",
            decl.getName(),
            additionalAtts,
            type.isLocal()?"":
                MessageFormat.format(" type=\"'{'{0}'}'{1}\"", type.getTargetNamespace(), type.getName()),
            decl.getFixedValue()==null ?
                "":" fixed=\""+decl.getFixedValue()+'\"',
            decl.getDefaultValue()==null ?
                "":" default=\""+decl.getDefaultValue()+'\"',
            type.isLocal()?"":" /"));

        if(type.isLocal()) {
            indent++;
            simpleType(type);
            indent--;
            println("</attribute>");
        }
    }

    public void simpleType( XSSimpleType type ) {
        println(MessageFormat.format("<simpleType{0}>", type.isLocal()?"":" name=\""+type.getName()+'\"'));
        indent++;

        type.visit((XSSimpleTypeVisitor)this);

        indent--;
		if (type.getBaseType().isLocal())
			println("</simpleType>");
    }

    public void listSimpleType( XSListSimpleType type ) {
        XSSimpleType itemType = type.getItemType();

        if(itemType.isLocal()) {
            println("<list>");
            indent++;
            simpleType(itemType);
            indent--;
            println("</list>");
        } else {
            // global type
            println(MessageFormat.format("<list itemType=\"'{'{0}'}'{1}\" />",
                itemType.getTargetNamespace(), itemType.getName()));
        }
    }

    public void unionSimpleType( XSUnionSimpleType type ) {
        final int len = type.getMemberSize();
        StringBuffer ref = new StringBuffer();

        for( int i=0; i<len; i++ ) {
            XSSimpleType member = type.getMember(i);
            if(member.isGlobal())
                ref.append(MessageFormat.format(" '{'{0}'}'{1}", member.getTargetNamespace(),member.getName()));
        }

        if(ref.length()==0)
            println("<union>");
        else
            println("<union memberTypes=\""+ref+"\">");
        indent++;

        for( int i=0; i<len; i++ ) {
            XSSimpleType member = type.getMember(i);
            if(member.isLocal())
                simpleType(member);
        }
        indent--;
        println("</union>");
    }

    public void restrictionSimpleType( XSRestrictionSimpleType type ) {

        if(type.getBaseType()==null) {
            // don't print anySimpleType
            if(!type.getName().equals("anySimpleType"))
                throw new InternalError();
            if(!Const.schemaNamespace.equals(type.getTargetNamespace()))
                throw new InternalError();
            return;
        }

        XSSimpleType baseType = type.getSimpleBaseType();

        println(MessageFormat.format("<restriction{0}>",
            baseType.isLocal()?"":" base=\"{"+
            baseType.getTargetNamespace()+'}'+
            baseType.getName()+'\"'));
        indent++;

        if(baseType.isLocal())
            simpleType(baseType);

        Iterator<XSFacet> itr = type.iterateDeclaredFacets();
        while(itr.hasNext())
            facet( (XSFacet)itr.next() );

        indent--;
		if (baseType.isLocal())
			println("</restriction>");
    }

    public void facet( XSFacet facet ) {
        println(MessageFormat.format("<{0} value=\"{1}\"/>",
            facet.getName(), facet.getValue()));
    }

    public void notation( XSNotation notation ) {
        println(MessageFormat.format("<notation name='\"0}\" public =\"{1}\" system=\"{2}\" />",
            notation.getName(), notation.getPublicId(), notation.getSystemId()));
    }



    public void complexType( XSComplexType type ) {
        println(MessageFormat.format("<complexType{0}>",
            type.isLocal()?"":" name=\""+type.getName()+'\"'));
        indent++;
        
        // TODO: wildcard
        
        if(type.getContentType().asSimpleType()!=null) {
            // simple content
            println("<simpleContent>");
            indent++;

            XSType baseType = type.getBaseType();

            if(type.getDerivationMethod()==XSType.RESTRICTION) {
                // restriction
                println(MessageFormat.format("<restriction base=\"<{0}>{1}\">",
                    baseType.getTargetNamespace(), baseType.getName()));
                indent++;

                dumpComplexTypeAttribute(type);

                indent--;
                println("</restriction>");
            } else {
                // extension
                println(MessageFormat.format("<extension base=\"<{0}>{1}\">",
                    baseType.getTargetNamespace(), baseType.getName()));

                // check if have redefine tag - Kirill
                if( type.isGlobal()
                && type.getTargetNamespace().equals(baseType.getTargetNamespace())
                && type.getName().equals(baseType.getName())) {
                    indent++;
                    println("<redefine>");
                    indent++;
                    baseType.visit(this);
                    indent--;
                    println("</redefine>");
                    indent--;
                }

                indent++;

                dumpComplexTypeAttribute(type);

                indent--;
                println("</extension>");
            }

            indent--;
            
            println("</simpleContent>");
        } else {
            // complex content
            //println("<complexContent>");
            indent++;

            XSComplexType baseType = type.getBaseType().asComplexType();

            if(type.getDerivationMethod()==XSType.RESTRICTION) {
                // restriction
//                println(MessageFormat.format("<restriction base=\"'{'{0}'}'{1}\">",
//                    baseType.getTargetNamespace(), baseType.getName()));
//                indent++;

                type.getContentType().visit(this);
                dumpComplexTypeAttribute(type);

//                indent--;
//                println("</restriction>");
            } else {
                // extension
                println(MessageFormat.format("<extension base=\"'{'{0}'}'{1}\">",
                    baseType.getTargetNamespace(), baseType.getName()));

                // check if have redefine - Kirill
                if( type.isGlobal()
                && type.getTargetNamespace().equals(baseType.getTargetNamespace())
                && type.getName().equals(baseType.getName())) {
                    indent++;
                    println("<redefine>");
                    indent++;
                    baseType.visit(this);
                    indent--;
                    println("</redefine>");
                    indent--;
                }

                indent++;

                type.getExplicitContent().visit(this);
                dumpComplexTypeAttribute(type);

                indent--;
                println("</extension>");
            }

            indent--;
            //println("</complexContent>");
        }

        indent--;
        println("</complexType>");
    }

    private void dumpComplexTypeAttribute( XSComplexType type ) {
        Iterator<?> itr;

        itr = type.iterateAttGroups();
        while(itr.hasNext())
            dumpRef( (XSAttGroupDecl)itr.next() );

        itr = type.iterateDeclaredAttributeUses();
        while(itr.hasNext())
            attributeUse( (XSAttributeUse)itr.next() );

        XSWildcard awc = type.getAttributeWildcard();
        if(awc!=null)
            wildcard("anyAttribute",awc,"");
    }

    public void elementDecl( XSElementDecl decl ) {
        elementDecl(decl,"");
    }
    
    
    
    
    private void elementDecl( XSElementDecl decl, String extraAtts) {
        XSType type = decl.getType();

        // TODO: various other attributes 
        
        //System.out.println(decl.getName()+ ":"+ );
       
        // qualified attr; Issue 
        if(decl.getForm() != null) {
            extraAtts += " form=\"" + (decl.getForm() ? "qualified" : "unqualified" ) + "\"";
        }

        if(type.isLocal()) {
        	if(!type.getBaseType().isLocal()){
        		
                if(this.elementsMap.containsKey(decl.getName())){
                	
                	println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>",
                            decl.getName(),
                            "",
                            extraAtts,
                            type.isLocal()?"":""));
                    indent++;
                    
                    if(type.isLocal())  type.visit(this);
                    
                    XmlElementBean changedBean = elementsMap.get(decl.getName());
                	if(changedBean.getElementName().equalsIgnoreCase(decl.getName())){
                		if(changedBean.getNullCheck().equalsIgnoreCase("cannotBeNull") && !changedBean.getRegex().isEmpty()){
                			//{http://www.w3.org/2001/XMLSchema}string
							if (!(type.getDerivationMethod() == XSType.RESTRICTION))
								println(MessageFormat.format("<restriction{0}>", type.isLocal() ? ""
										: " base=\"{" + type.getTargetNamespace() + '}' + type.getName() + '\"'));

//                			println(MessageFormat.format("<restriction base=\"{{0}}"+"{1}>\">",
//                            		type.getTargetNamespace(), type.getName()));
                                indent++;
                                println("<minLength value=\"1\"/>");
                                println(MessageFormat.format("<pattern value=\"{0}\"/>",changedBean.getRegex()));
                                    indent--;
                		}else if(!changedBean.getRegex().isEmpty()){
                                indent++;
                                if (!(type.getDerivationMethod() == XSType.RESTRICTION))
    								println(MessageFormat.format("<restriction{0}>", type.isLocal() ? ""
    										: " base=\"{" + type.getTargetNamespace() + '}' + type.getName() + '\"'));
                                indent++;
                                println(MessageFormat.format("<pattern value=\"{0}\"/>",changedBean.getRegex()));
                                    indent--;
                		}else if(changedBean.getNullCheck().equalsIgnoreCase("cannotBeNull")){
                			if (!(type.getDerivationMethod() == XSType.RESTRICTION))
								println(MessageFormat.format("<restriction{0}>", type.isLocal() ? ""
										: " base=\"{" + type.getTargetNamespace() + '}' + type.getName() + '\"'));
//                			println(MessageFormat.format("<restriction base=\"{{0}}"+"{1}>\">",
//                            		type.getTargetNamespace(), type.getName()));
                                indent++;
                                println("<minLength value=\"1\"/>");
                                    indent--;
                		}
                		println("</restriction>");
                        println("</simpleType>");
                    	
                    }
                }else{
                	
                	println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>",
                            decl.getName(),
                            type.isLocal()?"":" type=\"{"+
                            type.getTargetNamespace()+'}'+
                            type.getName()+'\"',
                            extraAtts,
                            type.isLocal()?"":""));
                    indent++;
                    
                    if(type.isLocal())  type.visit(this);
					if (type.getDerivationMethod() == XSType.RESTRICTION)
						println("</restriction>");
                    println("</simpleType>");
                }

                indent--;
                println("</element>");
            
            
        		
        	}else{
        	println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>",
                    decl.getName(),
                    type.isLocal()?"":" type=\"{"+
                    type.getTargetNamespace()+'}'+
                    type.getName()+'\"',
                    extraAtts,
                    type.isLocal()?"":"/"));
        	
            indent++;
            
            if(type.isLocal())  type.visit(this);
            
            /*if(this.elementList.containsKey(decl.getName())){
            	XsdValidationBean chnagedBean = elementList.get(decl.getName());
            	if(chnagedBean.getElementName().equalsIgnoreCase(decl.getName())){
            		if(chnagedBean.getNullCheck() != null && chnagedBean.getRegex() != null){
            			println("<simpleType>");
            			println(MessageFormat.format("<restriction base=\"<{0}>\">",
                        		type.getTargetNamespace(), type.getName()));
                            indent++;
                            println("<minLength value=\"1\"/>");
                            println(MessageFormat.format("<pattern value=\"{0}\"/>",chnagedBean.getRegex()));
                                indent--;
                                println("</restriction>");
                                println("</simpleType>");
            		}else if(chnagedBean.getRegex() != null){
            			println(MessageFormat.format("<restriction base=\"<{0}>\">",
                        		type.getTargetNamespace(), type.getName()));
                            indent++;
                            println(MessageFormat.format("<pattern value=\"{0}\"/>",chnagedBean.getRegex()));
                                indent--;
                                println("</restriction>");
            		}else if(chnagedBean.getNullCheck() != null){
            			println("<simpleType>");
            			println(MessageFormat.format("<restriction base=\"<{0}>\">",
                        		type.getTargetNamespace(), type.getName()));
                            indent++;
                            println("<minLength value=\"1\"/>");
                                indent--;
                                println("</restriction>");
                                println("</simpleType>");
            		}
                	
                }
            }*/

            indent--;
            println("</element>");
        	}
        }else{
        	
            if(this.elementsMap.containsKey(decl.getName())){
            	
            	println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>",
                        decl.getName(),
                        "",
                        extraAtts,
                        type.isLocal()?"":""));
                indent++;
                
                if(type.isLocal())  type.visit(this);
            	
            	
            	XmlElementBean changedBean = elementsMap.get(decl.getName());
            	if(changedBean.getElementName().equalsIgnoreCase(decl.getName())){
            		if(changedBean.getNullCheck().equalsIgnoreCase("cannotBeNull") && !changedBean.getRegex().isEmpty()){
            			println("<simpleType>");
            			//{http://www.w3.org/2001/XMLSchema}string
            			
            			println(MessageFormat.format("<restriction{0}>",
            					type.isLocal()?"":" base=\"{"+
            							type.getTargetNamespace()+'}'+
            							type.getName()+'\"'));
            			
//            			println(MessageFormat.format("<restriction base=\"{{0}}"+"{1}>\">",
//                        		type.getTargetNamespace(), type.getName()));
                            indent++;
                            println("<minLength value=\"1\"/>");
                            println(MessageFormat.format("<pattern value=\"{0}\"/>",changedBean.getRegex()));
                                indent--;
                                println("</restriction>");
                                println("</simpleType>");
            		}else if(!changedBean.getRegex().isEmpty()){
            			println("<simpleType>");
            			println(MessageFormat.format("<restriction{0}>",
            					type.isLocal()?"":" base=\"{"+
            							type.getTargetNamespace()+'}'+
            							type.getName()+'\"'));
//            			println(MessageFormat.format("<restriction base=\"{{0}}"+"{1}>\">",
//                        		type.getTargetNamespace(), type.getName()));
                            indent++;
                            println(MessageFormat.format("<pattern value=\"{0}\"/>",changedBean.getRegex()));
                                indent--;
                                println("</restriction>");
                                println("</simpleType>");
            		}else if(changedBean.getNullCheck().equalsIgnoreCase("cannotBeNull")){
            			println("<simpleType>");
            			println(MessageFormat.format("<restriction{0}>",
            					type.isLocal()?"":" base=\"{"+
            							type.getTargetNamespace()+'}'+
            							type.getName()+'\"'));
//            			println(MessageFormat.format("<restriction base=\"{{0}}"+"{1}>\">",
//                        		type.getTargetNamespace(), type.getName()));
                            indent++;
                            println("<minLength value=\"1\"/>");
                                indent--;
                                println("</restriction>");
                                println("</simpleType>");
            		}
                	
                }
            }else{
            	
            	println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>",
                        decl.getName(),
                        type.isLocal()?"":" type=\"{"+
                        type.getTargetNamespace()+'}'+
                        type.getName()+'\"',
                        extraAtts,
                        type.isLocal()?"":""));
                indent++;
                
                if(type.isLocal())  type.visit(this);
            	
            }

            indent--;
            println("</element>");
        
        }
        
        
        

    }

    public void modelGroupDecl( XSModelGroupDecl decl ) {
        println(MessageFormat.format("<group name=\"{0}\">", decl.getName()));
        indent++;

        modelGroup(decl.getModelGroup());

        indent--;
        println("</group>");
    }

    public void modelGroup( XSModelGroup group ) {
        modelGroup(group,"");
    }
    private void modelGroup( XSModelGroup group, String extraAtts ) {
        println(MessageFormat.format("<{0}{1}>", group.getCompositor(), extraAtts));
        indent++;

        final int len = group.getSize();
        for( int i=0; i<len; i++ )
            particle(group.getChild(i));

        indent--;
        println(MessageFormat.format("</{0}>", group.getCompositor()));
    }

    public void particle( XSParticle part ) {
        BigInteger i;

        StringBuilder buf = new StringBuilder();

        i = part.getMaxOccurs();
        if(i.equals(BigInteger.valueOf(XSParticle.UNBOUNDED)))
            buf.append(" maxOccurs=\"unbounded\"");
        else if(!i.equals(BigInteger.ONE))
            buf.append(" maxOccurs=\"").append(i).append('\"');

        i = part.getMinOccurs();
        if(!i.equals(BigInteger.ONE))
            buf.append(" minOccurs=\"").append(i).append('\"');

        final String extraAtts = buf.toString();

        part.getTerm().visit(new XSTermVisitor(){
            public void elementDecl( XSElementDecl decl ) {
                if(decl.isLocal())
                	JumbuneSchemaWriter.this.elementDecl(decl,extraAtts);
                else {
                    // reference
                    println(MessageFormat.format("<element ref=\"'{'{0}'}'{1}\"{2}/>",
                        decl.getTargetNamespace(),
                        decl.getName(),
                        extraAtts));
                }
            }
            public void modelGroupDecl( XSModelGroupDecl decl ) {
                // reference
                println(MessageFormat.format("<group ref=\"'{'{0}'}'{1}\"{2}/>",
                    decl.getTargetNamespace(),
                    decl.getName(),
                    extraAtts));
            }
            public void modelGroup( XSModelGroup group ) {
            	JumbuneSchemaWriter.this.modelGroup(group,extraAtts);
            }
            public void wildcard( XSWildcard wc ) {
            	JumbuneSchemaWriter.this.wildcard("any",wc,extraAtts);
            }
        });
    }

    public void wildcard( XSWildcard wc ) {
        wildcard("any",wc,"");
    }

    private void wildcard( String tagName, XSWildcard wc, String extraAtts ) {
        final String proessContents;
        switch(wc.getMode()) {
        case XSWildcard.LAX:
            proessContents = " processContents='lax'";break;
        case XSWildcard.STRTICT:
            proessContents = "";break;
        case XSWildcard.SKIP:
            proessContents = " processContents='skip'";break;
        default:
            throw new AssertionError();
        }

        println(MessageFormat.format("<{0}{1}{2}{3}/>",tagName, proessContents, wc.apply(WILDCARD_NS), extraAtts));
    }

    private static final XSWildcardFunction<String> WILDCARD_NS = new XSWildcardFunction<String>() {
        public String any(Any wc) {
            return ""; // default
        }

        public String other(Other wc) {
            return " namespace='##other'";
        }

        public String union(Union wc) {
            StringBuilder buf = new StringBuilder(" namespace='");
            boolean first = true;
            for (String s : wc.getNamespaces()) {
                if(first)   first=false;
                else        buf.append(' ');
                buf.append(s);
            }
            return buf.append('\'').toString();
        }
    };

    public void annotation( XSAnnotation ann ) {
        // TODO: it would be nice even if we just put <xs:documentation>
    }

    public void identityConstraint(XSIdentityConstraint decl) {
        // TODO
    }

    public void xpath(XSXPath xp) {
        // TODO
    }

    public void empty( XSContentType t ) {}
}



