package component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.BehaviorEvent;
import javax.faces.validator.ValidatorException;

@FacesComponent("harvey1")
public class HarveyInput extends HtmlInputText {
	
	public HarveyInput() {
		    AjaxBehavior ajaxBehavior = new AjaxBehavior();
		    ajaxBehavior.addAjaxBehaviorListener(this::handleAjaxEvent);
		    this.addClientBehavior("blur", ajaxBehavior);
	}
	
	public void handleAjaxEvent(BehaviorEvent event) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    Object submittedValue = getSubmittedValue();
	    Object convertedValue = getConvertedValue(context, submittedValue);
	    setValue(convertedValue);
	    validate(context);    
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
	    System.out.println("Begin");
	    ResponseWriter writer = context.getResponseWriter();
	    String clientId = getClientId(context);
	    writer.startElement("input", this);
	    writer.writeAttribute("id", clientId, "id");
	    writer.writeAttribute("name", clientId, "name");
	    writer.writeAttribute("type", "text", "type");
	    
	    Object value = getValue();
	    if (value instanceof Date) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        String dateString = dateFormat.format((Date) value);
	        writer.writeAttribute("value", dateString, "value");
	    } else {
	        writer.writeAttribute("value", value, "value");
	    }

	    Boolean requiredCondition = Boolean.parseBoolean((String) getAttributes().get("requiredInput"));
	    if (Boolean.TRUE.equals(requiredCondition)) {
	        writer.writeAttribute("required", "true", "required");
	    }

	    StringBuilder messageId = new StringBuilder();
	    messageId.append(clientId).append(":").append("message");

	    StringBuilder jsFunction = new StringBuilder();
	    jsFunction.append("mojarra.ab(this, event, 'valueChange', '")
	              .append(clientId)
	              .append("', '")
	              .append(clientId)
	              .append(" ")
	              .append(messageId)
	              .append("')");
	    System.out.println(jsFunction);
	    writer.writeAttribute("onblur", jsFunction.toString(), null);

	    writer.startElement("span", this);
	    writer.writeAttribute("id", messageId.toString(), "id");
	    writer.writeAttribute("for", clientId.toString(), "for");
	    writer.writeAttribute("style", "color:red;", null);

	}
    
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
    	System.out.println("End");
    	ResponseWriter writer = context.getResponseWriter();
        writer.endElement("input");
        writer.endElement("span");
    }
    
    private boolean isAjaxRequest(FacesContext context, String clientId) {
    	System.out.println("check ajax");
        String ajaxSource = context.getExternalContext().getRequestParameterMap().get("javax.faces.source");
        System.out.println(ajaxSource);
        return clientId.equals(ajaxSource);
    }

    @Override
    public void decode(FacesContext context) {
    	System.out.println("Decode");
        String clientId = getClientId(context);
        String value = context.getExternalContext().getRequestParameterMap().get(clientId);
        setSubmittedValue(value);
        
        if (isAjaxRequest(context, clientId)) {
            queueEvent(new AjaxBehaviorEvent(this, getClientBehaviors().get("blur").get(0)));
        }
        System.out.println("Decode finished");
    }

    @Override
    protected Object getConvertedValue(FacesContext context, Object submittedValue) {
        System.out.println("Convert");

        if (submittedValue == null) {
            return null;
        }

        String converterType = (String) getAttributes().get("type");
        String converterName = converterType + "Converter";
        Converter converter = context.getApplication().createConverter(converterName);

        if (converter == null) {
            return submittedValue;
        }
            Object convertedValue = converter.getAsObject(context, this, submittedValue.toString());
            return convertedValue;
    }


    @Override
    public void validate(FacesContext context) {
    	System.out.println("Validate");
        Object value = getValue();

        String requiredInputStr = (String) getAttributes().get("requiredInput");
        Boolean required = Boolean.parseBoolean(requiredInputStr);

        if (Boolean.TRUE.equals(required) && (value == null || value.toString().isEmpty())) {
            String message = (String) getAttributes().get("requiredMessageInput");
            if (message == null || message.isEmpty()) {
                message = "This field is required.";
            }
            throw new ValidatorException(new FacesMessage(message));
        }
        StringBuilder validatorName = new StringBuilder();
        validatorName.append((String) getAttributes().get("type")).append("Validator");       
        ELContext elContext = context.getELContext();
        ExpressionFactory factory = context.getApplication().getExpressionFactory();
        MethodExpression validatorMethod = factory.createMethodExpression(elContext,
                "#{employeeBean." + validatorName + "}", null, new Class[]{FacesContext.class, UIInput.class, Object.class});

        if (validatorMethod != null) {
            validatorMethod.invoke(elContext, new Object[]{context, this, value});
        }
        
        super.validate(context);
    }
}
