package de.hhu.sharing.web;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;

import de.hhu.sharing.model.NotFoundException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected String handleNotFoundException(NotFoundException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errMessage", ex.getMessage());
        return "redirect:/";
    }
    
    @ExceptionHandler(RollbackException.class)
    protected String handleRollbackException(RollbackException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errMessage", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(MultipartException.class)
    protected String handleFileException(HttpServletRequest request, Throwable ex, RedirectAttributes redirectAttributes) {
        if(request.getServletPath().equals("/handleFileUploadItem")) {
            redirectAttributes.addFlashAttribute("errMessage", ">Bild zu groß (über 150KB) oder falsches Format!");
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("errMessage", ">Bild zu groß (über 150KB) oder falsches Format!");
        return "redirect:/account";
    }
}