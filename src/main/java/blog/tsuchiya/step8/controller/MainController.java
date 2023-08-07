package blog.tsuchiya.step8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import blog.tsuchiya.step8.controller.form.SampleForm;
import blog.tsuchiya.step8.service.SampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final SampleService ss;
	@GetMapping
	public String index(Model model) {
		model.addAttribute("sampleForm", new SampleForm());
		return "index";
	}
	
	@PostMapping("input")
	public String input(@Valid SampleForm sampleForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "index";
		}
		int textLength = ss.length(sampleForm.getText());
		model.addAttribute("textLength", textLength);
		model.addAttribute("integer", sampleForm.getInteger());
		return "input";
	}
}
