package in.employee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="tax_detail")
public class TaxDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	 private int employeeId;
	@Column(name="first_name")
	    private String firstName;
	@Column(name="last_name")
	    private String lastName;
	@Column(name="yearly_salary")
	    private double yearlySalary;
	@Column(name="tax_amount")
	    private double taxAmount;
	@Column(name="cess_amount")
	    private double cessAmount;

}
