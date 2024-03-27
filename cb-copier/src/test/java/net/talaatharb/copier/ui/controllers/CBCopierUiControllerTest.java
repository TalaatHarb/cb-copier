package net.talaatharb.copier.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import javafx.scene.control.TextArea;

@ExtendWith(ApplicationExtension.class)
@ExtendWith(MockitoExtension.class)
class CBCopierUiControllerTest {

	@InjectMocks
	private CBCopierUiController controller;

	@Mock
	private TextArea queriesTextArea;

	@Mock
	private TextArea planTextArea;

	@Test
	void test() {
		assertTrue(true);
	}

}
