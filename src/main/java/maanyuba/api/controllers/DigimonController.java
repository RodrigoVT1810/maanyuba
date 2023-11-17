package maanyuba.api.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import maanyuba.api.models.RequestModel;
import maanyuba.api.models.ResponseModel;
import maanyuba.api.services.DigimonService;

@RestController
@RequestMapping("/digimon")
public class DigimonController {
	@Autowired
    private DigimonService digimonService;
    
	@GetMapping("/find/{parameter}")
    public String find(@PathVariable String parameter){
		ResponseModel response = this.digimonService.find(parameter);
		if(response.getStatus())
			return response.getMsg();
		else 
			return "Hubo un error: "+response.getMsg();
    }
    
    @GetMapping("/types/{parameter}")
    public String types(@PathVariable String parameter){
    	ResponseModel response = this.digimonService.types(parameter);
		if(response.getStatus())
			return "Tipo(s) del digimon: "+response.getMsg();
		else 
			return "Hubo un error: "+response.getMsg();
    }
    
    @PostMapping("/add")
    public String add(@RequestBody RequestModel digimon){
    	ResponseModel response = this.digimonService.add(digimon);
		if(response.getStatus())
			return response.getMsg();
		else 
			return response.getMsg();
    }
}