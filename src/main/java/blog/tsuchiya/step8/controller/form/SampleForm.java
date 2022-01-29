package blog.tsuchiya.step8.controller.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SampleForm {

	@NotBlank
	@Size(max=10)
	private String text;
	
	@Max(10)
	@Min(1)
	@NotNull
	private Integer integer;
}
