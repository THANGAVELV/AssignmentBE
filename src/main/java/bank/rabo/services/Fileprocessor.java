package bank.rabo.services;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface Fileprocessor {
	void processFile(List<String> inputPath, String destinationPath);
}
