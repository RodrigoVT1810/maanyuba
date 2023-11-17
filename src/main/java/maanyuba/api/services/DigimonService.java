package maanyuba.api.services;

import org.springframework.stereotype.Service;
import maanyuba.api.models.RequestModel;
import maanyuba.api.models.ResponseModel;

@Service
public interface DigimonService {
	ResponseModel find(String parameter);
	ResponseModel types(String parameter);
	ResponseModel add(RequestModel digimon);
}