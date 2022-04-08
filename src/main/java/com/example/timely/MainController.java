package com.example.timely;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

	@Autowired
	ProjectRepository projectRepository;

	@GetMapping("/")
	public String home() {

		return "index";
	}

	@PostMapping("/Start")
	public String start(Model model) {
		Project project = new Project();
		project.setStart_time("danas");
		project.setDuration("da");
		project.setName("mate");
		project.setId(0);
		project.setStop_time("sutra");
		projectRepository.save(project);
		return "index";
	}
}

