package test.mastermind.distributed.dispatchers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mastermind.distributed.StartControllerProxy;
import mastermind.distributed.dispatchers.FrameType;
import mastermind.models.Session;
import santaTecla.utils.TCPIP;

@RunWith(MockitoJUnitRunner.class)
public class StartControllerProxyTest {
	@Mock
	TCPIP tcpipMock;

	@Mock
	Session sessionMock;

	StartControllerProxy scp;

	@Before
	public void before() {
		this.scp = new StartControllerProxy(sessionMock, tcpipMock);
	}

	@Test
	public void startTest() {
		when(tcpipMock.receiveInt()).thenReturn(1);

		this.scp.start();
		verify(tcpipMock).send(FrameType.START.name());

	}

}