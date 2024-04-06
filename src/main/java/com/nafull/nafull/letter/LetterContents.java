package com.nafull.nafull.letter;

import java.util.Random;

public class LetterContents {
    private static final String TEMPLATE_01 =
        "%이름%님, 2박 3일간의 해커톤에 참여하시느라 정말 고생많으셨습니다!\n" +
        "멋진 성과와 경험을 쌓으셨을 거라 믿어 의심치 않습니다. 잘 해내셨어요!";

    private static final String TEMPLATE_02 =
        "해커톤을 완주하신 %이름% 님! 짧은 시간 안에 무엇인가를 이뤄내는 게 쉽지 않은 일인데, %이름% 님의 창의력과 끈기에 박수를 보냅니다~!\n" +
        "앞으로의 프로젝트에서도 멋진 성과를 기대할게요!";

    private static final String TEMPLATE_03 =
        "2박 3일간의 해커톤이라는 도전을 이루어낸 것 자체가 대단한 성취예요.\n" +
        "%이름% 님의 노력과 팀워크가 빛을 발했을 거라 생각합니다. 수고하셨어요!\n" +
        "이 경험이 앞으로 더 큰 도약의 발판이 되기를 바랍니다.";
    
    private static final String[] TEMPLATES = { TEMPLATE_01, TEMPLATE_02, TEMPLATE_03 };

    public static String generateContent(String name) {
        int random = new Random().nextInt(3);
        String selectedTemplate = TEMPLATES[random];
        return selectedTemplate.replaceAll("%이름%", name);
    }
}
