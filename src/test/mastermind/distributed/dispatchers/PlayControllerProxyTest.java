package test.mastermind.distributed.dispatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mastermind.distributed.PlayControllerProxy;
import mastermind.distributed.dispatchers.FrameType;
import mastermind.distributed.dispatchers.TCPIP;
import mastermind.models.Session;
import mastermind.types.Color;
import mastermind.types.Error;

@RunWith(MockitoJUnitRunner.class)
public class PlayControllerProxyTest {

	@Mock
	Session sessionMock;

	@Mock
	TCPIP tcpipMock;

	PlayControllerProxy pcp;

	@Before
	public void before() {

		this.pcp = new PlayControllerProxy(sessionMock, tcpipMock);

	}

	@Test
	@DisplayName("addProposedCombination should not return an error")
	public void addProposedCombinationTest() {
		when(tcpipMock.receiveError()).thenReturn(null);
		List<Color> colors = new ArrayList<Color>();
		colors.add(Color.BLUE);
		Error error = this.pcp.addProposedCombination(colors);
		verify(tcpipMock).send(colors.size());
		verify(tcpipMock).send(Color.BLUE);
		assertEquals(error, null);
		verify(tcpipMock).receiveError();
	}

	@Test
	@DisplayName("addProposedCombination should return DUPLICATED error")
	public void addProposedCombinationTestDuplicated() {
		when(tcpipMock.receiveError()).thenReturn(Error.DUPLICATED);
		List<Color> colors = new ArrayList<Color>();
		colors.add(Color.BLUE);
		Error error = this.pcp.addProposedCombination(colors);
		verify(tcpipMock).send(colors.size());
		verify(tcpipMock).send(Color.BLUE);
		assertEquals(error, Error.DUPLICATED);
		verify(tcpipMock).receiveError();
	}

	@Test
	@DisplayName("addProposedCombination should return WRONG_LENGTH error")
	public void addProposedCombinationTestWrongLength() {
		when(tcpipMock.receiveError()).thenReturn(Error.WRONG_LENGTH);
		List<Color> colors = new ArrayList<Color>();
		colors.add(Color.GREEN);
		Error error = this.pcp.addProposedCombination(colors);
		verify(tcpipMock).send(colors.size());
		verify(tcpipMock).send(Color.GREEN);
		assertEquals(error, Error.DUPLICATED);
		verify(tcpipMock).receiveError();
	}

	@Test
	@DisplayName("addProposedCombination should return WRONG_CHARACTERS error")
	public void addProposedCombinationTestWrongCharacters() {
		when(tcpipMock.receiveError()).thenReturn(Error.WRONG_CHARACTERS);
		List<Color> colors = new ArrayList<Color>();
		colors.add(Color.ORANGE);
		Error error = this.pcp.addProposedCombination(colors);
		verify(tcpipMock).send(colors.size());
		verify(tcpipMock).send(Color.ORANGE);
		assertEquals(error, Error.DUPLICATED);
		verify(tcpipMock).receiveError();
	}

	@Test
	@DisplayName("undoable should return true")
	public void undoableTest() {
		when(tcpipMock.receiveBoolean()).thenReturn(true);
		boolean result = this.pcp.undoable();
		verify(tcpipMock).send(FrameType.UNDOABLE.name());
		verify(tcpipMock).receiveBoolean();
		assertEquals(result, true);
	}

	@Test
	@DisplayName("redoable should return false")
	public void redoableTest() {
		when(tcpipMock.receiveBoolean()).thenReturn(false);
		boolean result = this.pcp.redoable();
		verify(tcpipMock).send(FrameType.REDOABLE.name());
		verify(tcpipMock).receiveBoolean();
		assertEquals(result, false);
	}

	@Test
	@DisplayName("isWinner should return true")
	public void isWinnerTest() {
		when(tcpipMock.receiveBoolean()).thenReturn(true);
		boolean result = this.pcp.isWinner();
		verify(tcpipMock).send(FrameType.WINNER.name());
		verify(tcpipMock).receiveBoolean();
		assertEquals(result, true);
	}

	@Test
	@DisplayName("isLooser should return false")
	public void isLooserTest() {
		when(tcpipMock.receiveBoolean()).thenReturn(false);
		boolean result = this.pcp.isLooser();
		verify(tcpipMock).send(FrameType.LOOSER.name());
		verify(tcpipMock).receiveBoolean();
		assertEquals(result, false);
	}

	@Test
	@DisplayName("getColors should return a list with three purples")
	public void getColorsTest() {
		int position = 2;
		when(tcpipMock.receiveInt()).thenReturn(3);
		when(tcpipMock.receiveColor()).thenReturn(Color.PURPLE);

		List<Color> colors = this.pcp.getColors(position);
		verify(tcpipMock).send(FrameType.COLORS.name());
		verify(tcpipMock).send(position);
		verify(tcpipMock).receiveColor();

		assertEquals(colors.size(), 3);
		assertEquals(colors.get(0), Color.PURPLE);
		assertEquals(colors.get(1), Color.PURPLE);
		assertEquals(colors.get(2), Color.PURPLE);
	}

	@Test
	@DisplayName("getColors should return an empty color list")
	public void getColorsTest2() {
		int position = 2;
		when(tcpipMock.receiveInt()).thenReturn(0);
		when(tcpipMock.receiveColor()).thenReturn(Color.PURPLE);

		List<Color> colors = this.pcp.getColors(position);
		verify(tcpipMock).send(FrameType.COLORS.name());
		verify(tcpipMock).send(position);

		assertEquals(colors.size(), 0);
	}

	@Test
	@DisplayName("getAttempts should return 2")
	public void getAttempts() {
		when(tcpipMock.receiveInt()).thenReturn(2);
		int result = this.pcp.getAttempts();
		verify(tcpipMock).send(FrameType.ATTEMPTS.name());
		verify(tcpipMock).receiveInt();
		assertEquals(result, 2);
	}

	@Test
	@DisplayName("getBlacks should return 1")
	public void getBlacks() {
		int position = 2;
		when(tcpipMock.receiveInt()).thenReturn(1);

		int result = this.pcp.getBlacks(position);
		verify(tcpipMock).send(FrameType.BLACKS.name());
		verify(tcpipMock).send(position);

		assertEquals(result, 1);
	}

	@Test
	@DisplayName("getWhites should return 1")
	public void getWhites() {
		int position = 2;
		when(tcpipMock.receiveInt()).thenReturn(1);

		int result = this.pcp.getBlacks(position);
		verify(tcpipMock).send(FrameType.WHITES.name());
		verify(tcpipMock).send(position);

		assertEquals(result, 1);
	}
}