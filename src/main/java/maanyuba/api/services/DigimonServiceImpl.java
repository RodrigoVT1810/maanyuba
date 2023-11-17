package maanyuba.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import maanyuba.api.models.RequestModel;
import maanyuba.api.models.ResponseModel;
import maanyuba.api.repositories.IDigimonRepository;

@Service
public class DigimonServiceImpl implements DigimonService{
	@Autowired
	private IDigimonRepository digimonRepository;

    public ResponseModel find(String parameter){
    	return this.digimonRepository.find(parameter);
    }
	
	public ResponseModel types(String parameter){
		return this.digimonRepository.types(parameter);
    }
	
	public ResponseModel add(RequestModel digimon){
		return this.digimonRepository.add(digimon);
    }
}