package com.elex_project.chimera;

import com.elex_project.chimera.api.LatestDataResponse;
import com.elex_project.chimera.exception.InvalidValueException;
import com.elex_project.chimera.pojo.Ltid;
import com.elex_project.chimera.pojo.UserKey;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ThingPlugTest {

	@Test @Disabled
	void getLatestData() throws InvalidValueException, IOException, InterruptedException {
		LatestDataResponse response = ThingPlug
				.getLatestData(Ltid.of("", ""),
						UserKey.of(""));

		System.out.println(response.toString());

	}
}