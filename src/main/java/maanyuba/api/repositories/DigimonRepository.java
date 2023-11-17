package maanyuba.api.repositories;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Repository;
import maanyuba.api.models.RequestModel;
import maanyuba.api.models.ResponseModel;
import maanyuba.api.models.DigimonModel;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class DigimonRepository implements IDigimonRepository
{
    Properties properties = loadProperties();
    String url = properties.getProperty("spring.datasource.url");
    String username = properties.getProperty("spring.datasource.username");
    String password = properties.getProperty("spring.datasource.password");
    
	private final String apiUrl = "https://www.digi-api.com/api/v1/digimon/";
    private final RestTemplate restTemplate;
    
    public DigimonRepository() {
        this.restTemplate = new RestTemplate();
    }
    
    public ResponseModel find(String parameter){
    	ResponseModel response = new ResponseModel();
		response = getApi(parameter);
    	return response;
    }
	
	public ResponseModel types(String parameter){
		ResponseModel response = new ResponseModel();
		
		response = getApi(parameter);
    	
    	if(response.getStatus()) {
			try {
		        ObjectMapper objectMapper = new ObjectMapper();
		        JsonNode jsonNode = objectMapper.readTree(response.getMsg());
		        JsonNode typesNode = jsonNode.get("types");
		        String result = "";
				if (typesNode != null && typesNode.isArray()) {
				    for (JsonNode type : typesNode) {
				        String typeDigimon = type.get("type").asText();
				        if(result == "")
				        	result += typeDigimon;
				        else
				        	result += ", "+typeDigimon;
				    }
				} else {
				    result = "El digimon no tipo tipo(s)";
				}
	            response.setMsg(result);
			} catch (JsonMappingException e) {
				response.setStatus(false);
				response.setMsg("Error en el servidor");
			} catch (JsonProcessingException e) {
				response.setStatus(false);
				response.setMsg("Error en el servidor");
			}
    	}
   
    	return response;
    }
	
	public ResponseModel add(RequestModel digimon){
		ResponseModel response = new ResponseModel();
		
		response = getApi(digimon.getParameter());
    	
    	if(response.getStatus()) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode jsonNode = objectMapper.readTree(response.getMsg());
	            DigimonModel model = new DigimonModel();
	            model.setDigimonId(Integer.parseInt(jsonNode.get("id").asText()));
	            model.setName(jsonNode.get("name").asText());
	            model.setResponse(response.getMsg());
	            response.setMsg(insert(model));
			} catch (JsonMappingException e) {
				response.setStatus(false);
				response.setMsg("Error en el servidor");
			} catch (JsonProcessingException e) {
				response.setStatus(false);
				response.setMsg("Error en el servidor");
			}
    	}
    	
    	return response;
    }
	
	public String insert(DigimonModel data) {
        String sql = "INSERT INTO digimons (digimon_id, name, response) VALUES (?, ?, ?)";
        
        try (
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, data.getDigimonId());
            preparedStatement.setString(2, data.getName());
            preparedStatement.setString(3, data.getResponse());

            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                return "Inserción exitosa. Filas afectadas: " + filasAfectadas;
            } else {
                return "No se realizaron inserciones.";
            }

        } catch (SQLException e) {
        	return "Error en el servidor";
        }
	}
	
	private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DigimonRepository.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

	private ResponseModel getApi(String parameter) {
		ResponseModel response = new ResponseModel();
    	response.setStatus(false);
    	
    	try {
            String result = restTemplate.getForObject(apiUrl+parameter, String.class);
            response.setStatus(true);
            response.setMsg(result);
    	}  catch (HttpClientErrorException.BadRequest e) {
    	    // Manejar la excepción Bad Request
    	    String responseBody = e.getResponseBodyAsString();

    	    try {
    	        // Mapear la respuesta JSON a un objeto Java
    	        ObjectMapper objectMapper = new ObjectMapper();
    	        JsonNode jsonNode = objectMapper.readTree(responseBody);
    	        String errorCode = jsonNode.get("error").asText();
    	        String errorMessage = jsonNode.get("message").asText();
    	        if(errorCode == "1")
    	        	response.setMsg(errorMessage);
    	        else
    	        	response.setMsg("Error en la solicitud: " + errorMessage);
    	    } catch (Exception ex) {
    	    	response.setMsg("Error en el servidor");
    	    }
    	} catch (Exception e) {
    		response.setMsg("Error en el servidor");
    	}
		
    	return response;
	}
}
