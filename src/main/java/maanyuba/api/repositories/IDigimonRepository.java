package maanyuba.api.repositories;

import org.springframework.stereotype.Component;
import maanyuba.api.models.ResponseModel;
import maanyuba.api.models.RequestModel;

@Component
public interface IDigimonRepository{
	ResponseModel find(String parameter);
	ResponseModel types(String parameter);
	ResponseModel add(RequestModel digimon);
}