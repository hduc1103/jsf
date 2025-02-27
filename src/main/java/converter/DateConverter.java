package converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "dateConverter")
public class DateConverter implements Converter<Object> {
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.trim().isEmpty()) {
            return null; 
        }
		SimpleDateFormat[] input = { new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("yyyy-MM-dd"),
				new SimpleDateFormat("yyyy/MM/dd"), new SimpleDateFormat("dd-MM-yyyy"),
				new SimpleDateFormat("MM/dd/yyyy"), new SimpleDateFormat("yy-dd-MM"),
				new SimpleDateFormat("yy/dd/MM"), new SimpleDateFormat("MM/yy/dd"),new SimpleDateFormat("MM-yy-dd"), new SimpleDateFormat("MM-dd-yy") };
		
		for (int i=0;i<input.length;i++) {
			input[i].setLenient(false);
		}
		value = value.trim().replace("-", "/");

		for (int i=0;i<input.length;i++) {
			try {
				return input[i].parse(value);
			} catch (ParseException e) {
			}
		}
	    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Date", "Please provide a valid date."));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

		if (value instanceof Date) {
			return output.format((Date) value);
		}

		return "";
	}
}
