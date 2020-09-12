package test.mastermind.distributed.dispatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mastermind.distributed.SessionProxy;
import mastermind.distributed.dispatchers.FrameType;
import mastermind.models.StateValue;
import santaTecla.utils.TCPIP;

@RunWith(MockitoJUnitRunner.class)
public class SessionProxyTest {

	@Mock
	TCPIP tcpipMock;

	SessionProxy sp;

	@Before
	public void before() {
		this.sp = new SessionProxy(tcpipMock);
	}

	@Test
	@DisplayName("resume should return IN_GAME state")
	public void getValueStateTest() {
		when(tcpipMock.receiveInt()).thenReturn(1);

		StateValue result = this.sp.getValueState();
		verify(tcpipMock).send(FrameType.STATE.name());
		verify(tcpipMock).receiveInt();

		assertEquals(result, StateValue.values()[1]);

	}

	@Test
	@DisplayName("getWidth should return 1")
	public void getWidthTest() {
		when(tcpipMock.receiveInt()).thenReturn(1);

		int result = this.sp.getWidth();
		verify(tcpipMock).send(FrameType.WIDTH.name());
		verify(tcpipMock).receiveInt();

		assertEquals(result, 1);

	}

}