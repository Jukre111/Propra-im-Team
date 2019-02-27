package de.hhu.sharing.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ErrorControllerMultipart extends ResponseEntityExceptionHandler {


@ExceptionHandler(MultipartException.class)
String handleFileException(HttpServletRequest request, Throwable ex, RedirectAttributes redirectAttributes) {
    if(request.getServletPath().equals("/handleFileUploadItem")) {
        redirectAttributes.addFlashAttribute("errMessage", ">Bild zu groß (über 150KB) oder falsches Format!");
        return "redirect:/";
    }
    redirectAttributes.addFlashAttribute("errMessage", ">Bild zu groß (über 150KB) oder falsches Format!");
    return "redirect:/account";
  }
}