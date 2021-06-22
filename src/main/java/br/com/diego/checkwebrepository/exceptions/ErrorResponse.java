package br.com.diego.checkwebrepository.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class ErrorResponse.
 * @author dcandido
 */
@JsonInclude(Include.NON_EMPTY)
public class ErrorResponse {
	
	/** The data. */
	private LocalDateTime data;
    
    /** The message. */
    private String message;
    
    @JsonProperty("campo_nome")
    private String fieldName;

    /**
     * Instantiates a new error response.
     *
     * @param message the message
     */
    public ErrorResponse(final String message) {
        this.data = LocalDateTime.now();
        this.message = message;
    }
    
    public ErrorResponse(final String fieldName, String message) {
        this.data = LocalDateTime.now();
        this.fieldName = fieldName;
        this.message = message;
    }
    
	/**
	 * Gets the message.
	 *
	 * @return the message	
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public LocalDateTime getData() {
		return data;
	}
}
