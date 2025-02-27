package model;

import java.util.Comparator;

public class EmployeeComparators {
    
    public static final Comparator<Employee> byName = (e1, e2) -> e1.getName().compareTo(e2.getName());

    public static final Comparator<Employee> byAge = Comparator.comparingInt(Employee::getAge);

   	public static class byDoB implements Comparator<Employee>{
		@Override
		public int compare(Employee e1, Employee e2) {
			return e1.getDob().compareTo(e2.getDob());
		}
   		
   	}
}
