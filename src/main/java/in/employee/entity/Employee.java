package in.employee.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
@Entity
@Table(name="Employee_dtl")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "emp_id")
	@JsonIgnore
	    private int employeeId;
	
	@Column(name="first_name")
	    private String firstName;
	
	@Column(name="last_name")
	    private String lastName;
	
	    private String email;
	    
	
	    @Pattern(regexp = "(^$|[0-9]{10})", message = "please pass only numeeric/digits ")
		@NotEmpty(message = "mobile number can't be empty")
		@Column(unique = true)

	   @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = Mobile.class)
	   @JoinColumn(name = "emp_fk",referencedColumnName = "emp_id")
	    private List<Mobile> mobiles;
		
	    @Column(name = "date_of_joining")
		//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "mm/dd/yyyy")
	
	 //@javax.validation.constraints.NotNull(message  = "INVALI_DATE_FORMAT")
	    private String doj;
	    
	    private double salary;
	    


	  
	}




