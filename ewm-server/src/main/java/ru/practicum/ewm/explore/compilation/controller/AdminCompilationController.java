package ru.practicum.ewm.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.compilation.dto.CompilationDto;
import ru.practicum.ewm.explore.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.explore.compilation.model.Compilation;
import ru.practicum.ewm.explore.compilation.service.CompilationService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Compilation createCompilation(@RequestBody @Valid UpdateCompilationDto updateCompilationDto) {
        return service.createCompilation(updateCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody UpdateCompilationDto updateCompilationDto) {
        return service.updateCompilation(compId, updateCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {
        service.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}