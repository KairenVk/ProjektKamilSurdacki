package com.tss.assemblers;

import com.tss.controllers.TaskRestController;
import com.tss.entities.data.Task;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {
    @Override
    public EntityModel<Task> toModel(Task task) {
        return EntityModel.of(task,
                linkTo(methodOn(TaskRestController.class).getTask(task.getId())).withSelfRel(),
                linkTo(methodOn(TaskRestController.class).all(task.getTaskList().getId())).withRel("tasks"));
    }
}
