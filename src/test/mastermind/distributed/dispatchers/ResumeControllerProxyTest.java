package test.mastermind.distributed.dispatchers;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import mastermind.distributed.ResumeControllerProxy;
import mastermind.distributed.dispatchers.FrameType;
import mastermind.distributed.dispatchers.TCPIP;
import mastermind.models.Session;

import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResumeControllerProxyTest {

	@Mock
	Session sessionMock;

	@Mock
	TCPIP tcpipMock;

	ResumeControllerProxy rcp;

	@Before
	public void before() {
		this.rcp = new ResumeControllerProxy(sessionMock, tcpipMock);
	}

	@Test
	@DisplayName("resume should not return an error")
	public void resumeTest() {
		boolean newGame = true;
		this.rcp.resume(newGame);
		verify(tcpipMock).send(FrameType.NEW_GAME.name());
		verify(tcpipMock).send(newGame);
	}

}