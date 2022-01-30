package blog.tsuchiya.step8.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import blog.tsuchiya.step8.service.SampleService;

// 最重要。テスト対象のサーバを起動して、Controllerの
// テストを行えるようにするアノテーション
@SpringBootTest
class MainControllerTest {
	
	// MainControllerでSampleServiceにMockオブジェクトをDIする
	@MockBean
	private SampleService ss;

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@BeforeEach
	void setup() {
		// @AutoConfigureMockMvcというアノテーションを使うとこの初期化は不要だが、
		// 問題が起きることもあるので手動で初期化している。
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@DisplayName("/を表示した場合")
	void indexSuccess() throws Exception {
		// @formatter:off
		// /にアクセスした場合のテストを行う
        this.mockMvc.perform(get("/"))
        		// modelにsampleFormという名前でオブジェクトが格納されていることを確認
                .andExpect(model().attributeExists("sampleForm"));
        // @formatter:on
	}

	@Test
	@DisplayName("/inputに妥当な入力をした場合")
	void inputSuccess()  throws Exception {
		// @MockBeanで格納されるMockオブジェクトはMockitoのもの。
		// そのため、使い方はMockitoと同じ。
		doReturn(5).when(ss).length("testInput");
		// @formatter:off
		// /inputにPostでアクセス、指定したパラメータを入力
		this.mockMvc.perform(post("/input").param("text", "testInput").param("integer", "4"))
				// modelのtextLengthの値は5
				.andExpect(model().attribute("textLength", 5))
				// modelのintegerの値は4
				.andExpect(model().attribute("integer", 4))
				// メソッドの戻り値はinput
				.andExpect(view().name("input"));
        // @formatter:on
		verify(ss, times(1)).length("testInput");
	}

	@Test
	@DisplayName("/inputに長過ぎる文字列を入力した場合")
	void inputTooLongText()  throws Exception {
		// @formatter:off
		// textに10文字を超える入力をしてみる
		this.mockMvc.perform(post("/input").param("text", "too_long_for_valid").param("integer", "4"))
				// エラーが1つあるはず
				.andExpect(model().errorCount(1))
				// modelに値は格納されていないはず
				.andExpect(model().attributeDoesNotExist("textLength"))
				.andExpect(model().attributeDoesNotExist("integer"))
				// エラー発生時はindexを表示する
				.andExpect(view().name("index"));
        // @formatter:on
		verify(ss, times(0)).length("too_long_for_valid");
		// setupメソッドでmockMvcを初期化しているので、DIしたモックオブジェクトも初期化されている
		verify(ss, times(0)).length("testInput");
	}

}
