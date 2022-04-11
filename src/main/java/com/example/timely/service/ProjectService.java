package com.example.timely.service;

import com.example.timely.model.Project;
import com.example.timely.model.paging.Page;
import com.example.timely.model.paging.Paged;
import com.example.timely.model.paging.Paging;
import com.example.timely.repository.ProjectRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    public Paged<Project> getProjectsPaged(int pageNumber, int size) {

        List<Project> projects = projectRepository.findAll();
        Collections.reverse(projects);
        int totalPages = ( (projects.size() - 1 ) / size ) +1 ;
        int skip = pageNumber > 1 ? (pageNumber - 1) * size : 0;
        List<Project> paged = projects.stream()
                                        .skip(skip)
                                        .limit(size)
                                        .collect(Collectors.toList());

        return new Paged<>(new Page<>(paged, totalPages), Paging.of(totalPages, pageNumber, size));
    }

    public void save(Project project)
    {
        projectRepository.save(project);
    }

    public Project getLast()
    {
        return projectRepository.findAll().get(projectRepository.findAll().size()-1);
    }

    public List<Project> getProjects()
    {
        return projectRepository.findAll();
    }

    public void deleteProjectById(Integer id)
    {
        projectRepository.deleteById(id);
    }

    public void setStopTime(String name) throws ParseException {
        Project project = getLast();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        project.setStop(formatter.format(calendar.getTime()));
        project.setCompleted(true);
        project.setName(name);

        String time1 = project.getStart();
        String time2 = project.getStop();

        Date date1 = formatter.parse(time1);
        Date date2 = formatter.parse(time2);

        long durationInMillis = date2.getTime() - date1.getTime();

        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d", hour, minute);
        project.setDuration(time);
        save(project);
    }

    public void setStartTime()
    {
        Project project = new Project();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        project.setStart(formatter.format(calendar.getTime()));

        save(project);
    }

    public void updateProject(Project project) {
        save(project);
    }

    public void deleteAll()
    {
        projectRepository.deleteAll();
    }

    public void export() throws IOException {
        List<Project> projects = projectRepository.findAll();
        Collections.reverse(projects);
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet spreadsheet = workbook.createSheet(" Timely Data ");

        XSSFRow row;

        Map<String, Object[]> timelyData
                = new TreeMap<String, Object[]>();

        timelyData.put("1", new Object[]{"Project name", "Start date", "Stop date", "Duration"});

        int i = 1;
        for (Project project : projects) {
            i=i+1;
            timelyData.put(
                    String.valueOf(i),
                    new Object[]{project.getName(), project.getStart(), project.getStop(), project.getDuration()});
        }

        Set<String> keyid = timelyData.keySet();


        int rowid = 0;

        for (String key : keyid) {

            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = timelyData.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String) obj);
            }
        }


        FileOutputStream out = new FileOutputStream(
                new File("timely.xlsx"));

        workbook.write(out);
        out.close();
    }


}
