package cybereast.service;

import com.google.cloud.speech.v1p1beta1.*;
import com.google.protobuf.ByteString;
import cybereast.model.UserModel;
import cybereast.model.UserRole;
import cybereast.model.UserTokenModel;
import cybereast.payload.UserLoginPayload;
import cybereast.payload.UserRegisterPayload;
import cybereast.repository.UserRepository;
import cybereast.repository.UserTokenRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ws.schild.jave.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    public Pair<String, UserModel> register(UserRegisterPayload payload) {

        if (!payload.getPassword().equals(payload.getRepeatedPassword())) {
            throw new SecurityException("Passwords are not equal!");
        }

        UserModel user = UserModel.builder()
                .userName(payload.getUserName())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .role(payload.getRole())
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .age(payload.getAge())
                .build();

        userRepository.save(user);

        UserTokenModel token = UserTokenModel.builder().token(RandomStringUtils.random(128)).user(user).build();
        userTokenRepository.save(token);

        return Pair.of(token.getToken(), user);

    }

    public Pair<String, UserModel> login(UserLoginPayload payload) {

        UserModel userModel = userRepository.findByEmailOrUserName(payload.getUserName(), payload.getUserName());

        if (!userModel.getPassword().equals(payload.getPassword())) {
            throw new SecurityException("Wrong password!");
        }

        UserTokenModel token = UserTokenModel.builder().token(RandomStringUtils.random(128)).user(userModel).build();
        userTokenRepository.save(token);

        return Pair.of(token.getToken(), userModel);

    }

    public UserModel getUser(String tokenCookie) {

        UserTokenModel token = userTokenRepository.findByToken(tokenCookie);

        return token.getUser();

    }

    public boolean checkRole(UserRole role, String tokenCookie) {

        UserTokenModel token = userTokenRepository.findByToken(tokenCookie);

        return token.getUser().getRole().equals(role);

    }

    public void transcribeFileWithAutomaticPunctuation() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        File source = new File(classLoader.getResource("files/testVideo.mov").getFile());
        File target = new File(classLoader.getResource("files/testAudio.flac").getFile());
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("flac");
        audio.setBitRate(new Integer(24000));
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(16000));
        EncodingAttributes attrs = new EncodingAttributes();
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setTag("DIVX");
        video.setBitRate(new Integer(160000));
        video.setFrameRate(new Integer(30));
        attrs.setFormat("flac");
        attrs.setVideoAttributes(video);
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        MultimediaObject multimediaObject = new MultimediaObject(source);
        try {
            target.setWritable(true);
            encoder.encode(multimediaObject, target, attrs);
        } catch (IllegalArgumentException | EncoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        Path path = Paths.get(fileName);

        byte[] content = Files.readAllBytes(target.toPath());

        Files.write(target.toPath(), content, StandardOpenOption.WRITE);

        try (SpeechClient speechClient = SpeechClient.create()) {
            // Configure request with local raw PCM audio
            RecognitionConfig recConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("ru-RU")
                            .setSampleRateHertz(16000)
                            .setEnableAutomaticPunctuation(true)
                            .build();

            // Get the contents of the local audio file
            RecognitionAudio recognitionAudio =
                    RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(content)).build();

            // Perform the transcription request
            RecognizeResponse recognizeResponse = speechClient.recognize(recConfig, recognitionAudio);

            // Just print the first result here.
            SpeechRecognitionResult result = recognizeResponse.getResultsList().get(0);

            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);

            // Print out the result
            System.out.printf("Transcript : %s\n", alternative.getTranscript());
        }
    }

}
