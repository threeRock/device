package io.jianxun.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jianxun.service.BusinessException;
import io.jianxun.web.dto.ReturnDto;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ReturnDto processValidationError(BindException ex) {
		ex.printStackTrace();
		BindingResult result = ex.getBindingResult();
		return processFieldError(result.getFieldErrors());
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ReturnDto processServcieExcptionError(BusinessException ex) {
		ex.printStackTrace();
		return ReturnDto.error(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ReturnDto processExcptionError(Exception ex) {
		ex.printStackTrace();
		return ReturnDto.error(ex.getMessage());
	}

	private ReturnDto processFieldError(List<FieldError> errors) {
		ReturnDto re = null;
		if (errors != null && errors.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (FieldError error : errors) {
				String msg = error.getDefaultMessage();
				sb.append(msg);

			}
			re = ReturnDto.error(sb.toString());
		}
		return re;
	}

}
