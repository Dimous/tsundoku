package io.github.dimous.tsundoku.presentation.view;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.github.dimous.tsundoku.application.IResourceDisposerInteractor;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

public final class Util {
    @Inject
    private Injector
        __injector;

    @Inject
    private ResourceBundle
        __resource_bundle;

    @Inject
    private IResourceDisposerInteractor
        __resource_disposer_interactor;

    public void start(final Stage __stage, final String __string_layout_name, final String __string_title, final double __double_width, final double __double_height) throws IOException {
        __stage.setTitle(__string_title);
        __stage.setOnCloseRequest(
            __window_event -> {
                this.__resource_disposer_interactor.dispose();
            }
        );
        __stage.setScene(
            new Scene(this.loadFXML(__string_layout_name), __double_width, __double_height)
        );
        __stage.show();
    }
    //---

    public <T> T loadFXML(final String __string_layout_name) throws IOException {
        final FXMLLoader
            __f_x_m_l_loader = new FXMLLoader(this.getClass().getClassLoader().getResource(__string_layout_name.concat(".fxml")), this.__resource_bundle);
        ///
        ///
        __f_x_m_l_loader.setControllerFactory(this.__injector::getInstance);

        return __f_x_m_l_loader.load();
    }
    //---

    public Optional<ButtonType> showAlert(final Alert.AlertType __alert_type, final String __string_title, final String __string_header_text, final String __string_content_text) {
        final Alert
            __alert = new Alert(__alert_type);
        ///
        ///
        __alert.setTitle(__string_title);
        __alert.setHeaderText(__string_header_text);
        __alert.setContentText(__string_content_text);

        return __alert.showAndWait();
    }
    //---

    public void showModal(final String __string_layout_name, final String __string_title, final double __double_width, final double __double_height) {
        final Stage
            __stage = new Stage();
        ///
        ///
        __stage.setResizable(false);
        __stage.setTitle(__string_title);
        __stage.setWidth(__double_width);
        __stage.setHeight(__double_height);
        __stage.initModality(Modality.APPLICATION_MODAL);

        try {
            __stage.setScene(
                new Scene(this.loadFXML(__string_layout_name), __double_width, __double_height)
            );
            __stage.showAndWait();
        } catch (final IOException __exception) {
            // __exception.printStackTrace();
        }
    }
    //---

    public Optional<ImageView> retrieveCover(final BookEntity __book_entity) {
        return this.retrieveCover(__book_entity, 150);
    }

    public Optional<ImageView> retrieveCover(final BookEntity __book_entity, final int __int_fit_width) {
        return switch (__book_entity.getExtension().toLowerCase()) { // тика поддерживает множество форматов
            case "pdf" -> this.retrieveCoverFromPDF(__book_entity.getPath(), __int_fit_width);
            case "epub" -> this.retrieveCoverFromEPUB(__book_entity.getPath(), __int_fit_width);
            default -> Optional.empty();
        };
    }
    //---

    private Optional<ImageView> retrieveCoverFromPDF(final String __string_file, final int __int_fit_width) {
        try (
            final PDDocument
                __p_d_document = Loader.loadPDF(new File(__string_file))
        ) {
            final PDResources
                __p_d_resources = __p_d_document.getPage(0).getResources();
            ///
            ///
            for (final COSName __c_o_s_name : __p_d_resources.getXObjectNames()) {
                final COSStream
                    __c_o_s_stream = __p_d_resources.getXObject(__c_o_s_name).getCOSObject();
                ///
                ///
                if (__c_o_s_stream.containsKey(COSName.SUBTYPE) && COSName.IMAGE.equals(__c_o_s_stream.getCOSName(COSName.SUBTYPE))) {
                    final ImageView
                        __image_view;
                    final BufferedImage
                        __buffered_image = ((PDImageXObject) PDImageXObject.createXObject(__c_o_s_stream, __p_d_resources)).getImage();
                    final int
                        __int_width = __buffered_image.getWidth(),
                        __int_height = __buffered_image.getHeight();
                    final WritableImage
                        __writable_image = new WritableImage(__int_width, __int_height);
                    final PixelWriter
                        __pixel_writer = __writable_image.getPixelWriter();
                    ///
                    ///
                    for (int __int_x = 0; __int_x < __int_width; ++__int_x) {
                        for (int __int_y = 0; __int_y < __int_height; ++__int_y) {
                            __pixel_writer.setArgb(__int_x, __int_y, __buffered_image.getRGB(__int_x, __int_y));
                        }
                    }

                    __image_view = new ImageView(__writable_image);

                    __image_view.setPreserveRatio(true);
                    __image_view.setFitWidth(__int_fit_width);

                    return Optional.of(__image_view);
                }
            }
        } catch (final Exception __exception) {
            // __exception.printStackTrace();
        }

        return Optional.empty();
    }
    //---

    private Optional<ImageView> retrieveCoverFromEPUB(final String __string_file, final int __int_fit_width) {
        // смотреть как в epublib извлекается обложка и пытаться средствами tika извлечь то же самое

        return Optional.empty();
    }
}
