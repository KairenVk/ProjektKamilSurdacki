package com.tss.assemblers;

import com.tss.controllers.BoardRestController;
import com.tss.controllers.UserRestController;
import com.tss.entities.RestForm;
import com.tss.entities.data.Board;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Component
public class BoardModelAssembler implements RepresentationModelAssembler<Board, EntityModel<Board>> {
    @Override
    public EntityModel<Board> toModel(Board board) {
        return EntityModel.of(board,
                linkTo(methodOn(UserRestController.class).getUser(board.getOwner().getId())).withRel("owner"),
                linkTo(methodOn(BoardRestController.class).getBoard(board.getId())).withSelfRel(),
                linkTo(methodOn(BoardRestController.class).all()).withRel("boards"));
    }

}
