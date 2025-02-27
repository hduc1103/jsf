package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "codeConverter")
public class CodeConverter implements Converter<Object>{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value ==null || value.trim().isEmpty()) {
		return null;}
		StringBuilder res= new StringBuilder();
		for(int i=0;i<value.length();i++) {
			res.append(Character.toUpperCase(value.charAt(i)));
		}
		return res.toString();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}
	
}
