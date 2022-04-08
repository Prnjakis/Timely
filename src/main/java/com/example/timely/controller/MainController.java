package com.example.timely.controller;

import com.example.timely.model.Project;
import com.example.timely.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {

	@Autowired
	ProjectRepository projectRepository;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("projects", projectRepository.findAll());
		if (projectRepository.findAll().isEmpty())
		{
			model.addAttribute("completed",true);
		}
		else
		{
			Project completed = projectRepository.findAll().get(projectRepository.findAll().size()-1);
			model.addAttribute("completed",completed.getCompleted());
		}
		model.addAttribute("name","");
		return "index";
	}

	@PostMapping("/Start")
	public String start(Model model) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Project project = new Project();
		project.setStart(formatter.format(calendar.getTime()));

		projectRepository.save(project);
		if (projectRepository.findAll().isEmpty())
		{
			model.addAttribute("completed",true);
		}
		else
		{
			Project completed = projectRepository.findAll().get(projectRepository.findAll().size()-1);
			model.addAttribute("completed",completed.getCompleted());
		}
		model.addAttribute("name",new String());
		model.addAttribute("projects", projectRepository.findAll());
		return "redirect:/";

	}

	@PostMapping("/Stop")
	public String stop(Model model,@ModelAttribute("name")String name) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Project project = projectRepository.findAll().get(projectRepository.findAll().size()-1);
		project.setStop(formatter.format(calendar.getTime()));
		project.setCompleted(true);
		project.setName(name);
		String time1 = project.getStart();
		String time2 = project.getStop();

		Date date1 = formatter.parse(time1);
		Date date2 = formatter.parse(time2);
		long difference = date2.getTime() - date1.getTime();
		project.setDuration(difference);
		projectRepository.save(project);
		model.addAttribute("projects", projectRepository.findAll());
		model.addAttribute("completed",true);
		return "redirect:/";
	}
}

