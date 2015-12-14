import java.io.IOException;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;



public class XPath2 {

	Element root;
	
	public XPath2(String filename)
	{
		init(filename);
	}
	
	public Object getPath(String path)
	{
		Element e = root;
		
		int pcounter = 0, mcounter = path.split("/").length;
		
		for(String part:path.split("/"))
		{
			int mode = 0;
			String attrib=null, value=null;
			int i = -1;
			
			if(part.equals(root.getName())) continue;
			
			String index = new String();
			if(part.endsWith("]"))
			{
				index = part.substring(part.indexOf("[")+1,part.indexOf("]"));
				if(index.startsWith("@"))
				{
					attrib = index.substring(1).split("=")[0];
					value = index.substring(1).split("=")[1];
					mode = 2;
					
					
				}
				else
				{
					mode = 16;
					i = Integer.parseInt(index);
				}
				
				part = part.substring(0, part.indexOf("["));
			}
			
			if(part.equals("text()"))
			{
				mode = 4;
			}
			
			if(part.startsWith("@"))
			{
				mode = 8;
				attrib = part.substring(1);
			}
			
			if(part.equals("count()"))
			{
				mode = 32;
			}
					
			
			List<Element> l = e.getChildren(part, e.getNamespace(e.getName()));
			if(mode == 0) {
				
				e = l.get(0);
				
				if(pcounter==mcounter-2) return e;
				
			}
			else if (mode == 2){
				for(Element lx:l)
				{
					if(lx.getAttributeValue(attrib).equals(value))
					{
						e = lx;
						mode = 0;
						if(pcounter==mcounter-2) return e;
					}
				}
			}
			else if(mode == 4)
			{
				return e.getText();
				
			}
			else if(mode == 8)
			{
				
				List<Attribute> la = e.getAttributes();
				for(Attribute a:la)
				{
					if(a.getName().equals(attrib))
					{
						return e.getAttribute(attrib,a.getNamespace()).getValue();
					}
				}
			}
			else if(mode == 16)
			{
				e = l.get(i);
				mode = 0;
				if(pcounter==mcounter-2) return e;
			}
			else if(mode == 32)
			{
				return ((Element)e.getParent()).getChildren().size();
				
			}
			
			pcounter++;
		
		}
		
		return null;
	}

	
	private void init(String filename)
	{
		try {
			Document doc = new SAXBuilder().build(filename);
			root = doc.getRootElement();
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
