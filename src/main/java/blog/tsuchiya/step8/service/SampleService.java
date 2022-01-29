package blog.tsuchiya.step8.service;

import org.springframework.stereotype.Service;

/**
 * ControllerにDIする対象。
 * 実装自体に意味はない。
 */
@Service
public class SampleService {

	public int length(String target) {
		return target.length();
	}
}
