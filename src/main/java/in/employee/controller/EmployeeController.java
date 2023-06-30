package in.employee.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.employee.entity.Employee;
import in.employee.exception.ResourceNotFoundException;
import in.employee.repository.EmployeeRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	private Map<String, Employee> employees = new HashMap<>();

	@PostMapping(path = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws Exception {

		// Store the employee details
		Employee empObj = new Employee();
		empObj.setFirstName(employee.getFirstName());
		empObj.setLastName(employee.getLastName());
		empObj.setEmail(employee.getEmail());
		empObj.setDoj(employee.getDoj());
		empObj.setSalary(employee.getSalary());
		empObj.setMobiles(employee.getMobiles());

		return new ResponseEntity<Employee>(employeeRepository.save(empObj), HttpStatus.CREATED);
	}

	@GetMapping("tax/{employeeId}/")
	public ResponseEntity<Map<String, Double>> calculateTaxDeduction(@PathVariable String employeeId) {
		Optional<Employee> empOpt = employeeRepository.findById(employeeId);
		if (!empOpt.isPresent()) {
			throw new ResourceNotFoundException("User not found with id: " + employeeId);
		}
		// Retrieve the employee details
		Employee employee = employees.get(employeeId);
		if (employee == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		// Calculate tax deduction
		double yearlySalary = calculateYearlySalary(employee);
		double taxAmount = calculateTaxAmount(yearlySalary);
		double cessAmount = calculateCessAmount(yearlySalary);

		// Prepare the response
		Map<String, Double> taxDetails = new HashMap<>();
		taxDetails.put("yearlySalary", yearlySalary);
		taxDetails.put("taxAmount", taxAmount);
		taxDetails.put("cessAmount", cessAmount);

		return ResponseEntity.ok(taxDetails);
	}

	private double calculateYearlySalary(Employee employee) {
		LocalDate currentDate = LocalDate.now();
		LocalDate joinDate = LocalDate.parse(employee.getDoj());

		int months = 12 * (currentDate.getYear() - joinDate.getYear())
				+ (currentDate.getMonthValue() - joinDate.getMonthValue());

		if (currentDate.getDayOfMonth() < joinDate.getDayOfMonth()) {
			months--;
		}

		double totalSalary = employee.getSalary() * months;
		return totalSalary;
	}

	private double calculateTaxAmount(double yearlySalary) {
		if (yearlySalary <= 250000) {
			return 0;
		} else if (yearlySalary <= 500000) {
			return 0.05 * (yearlySalary - 250000);
		} else if (yearlySalary <= 1000000) {
			return 0.1 * (yearlySalary - 500000) + 12500;
		} else {
			return 0.2 * (yearlySalary - 1000000) + 112500;
		}
	}

	private double calculateCessAmount(double yearlySalary) {
		if (yearlySalary > 2500000) {
			return 0.02 * (yearlySalary);
		}
		return 0.0;
	}
}
