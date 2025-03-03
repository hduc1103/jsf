package interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Loggable 
public class LoggingInterceptor {

    @AroundInvoke  
    public Object logMethodEntryExit(InvocationContext ctx) throws Exception {
        String className = ctx.getTarget().getClass().getName();
        String methodName = ctx.getMethod().getName();
        
        System.out.println("Entering method: " + className + "." + methodName);
        
        Object result = ctx.proceed();
        
        System.out.println("Exiting method: " + className + "." + methodName);
        
        return result;
    }
}
