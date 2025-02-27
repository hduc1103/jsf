package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "nameConverter")
public class NameConverter implements Converter<Object>{
	 @Override
	    public Object getAsObject(FacesContext context, UIComponent component, String value) {
	        if (value == null || value.trim().isEmpty()) {
	            return null;
	        }
	        String[] arrName = value.trim().toLowerCase().split("\\s+");
	        StringBuilder res = new StringBuilder();
	        for (String word : arrName) {
	            res.append(Character.toUpperCase(word.charAt(0)))
	                         .append(word.substring(1))
	                         .append(" ");
	        }
	        return res.toString().trim();
	    }

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}
	
}