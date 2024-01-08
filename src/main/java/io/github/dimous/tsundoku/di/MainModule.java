package io.github.dimous.tsundoku.di;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.github.dimous.tsundoku.application.DefaultBookInteractor;
import io.github.dimous.tsundoku.application.DefaultConfigInteractor;
import io.github.dimous.tsundoku.application.IBookInteractor;
import io.github.dimous.tsundoku.application.IConfigInteractor;
import io.github.dimous.tsundoku.data.data_source.*;
import io.github.dimous.tsundoku.data.repository.DefaultBookRepository;
import io.github.dimous.tsundoku.data.repository.DefaultConfigRepository;
import io.github.dimous.tsundoku.data.service.*;
import io.github.dimous.tsundoku.domain.contract.IBookRepository;
import io.github.dimous.tsundoku.domain.contract.IConfigRepository;
import io.github.dimous.tsundoku.presentation.view.Util;
import io.github.dimous.tsundoku.presentation.view.control.DuplicatesTreeCell;
import io.github.dimous.tsundoku.presentation.view.control.IdenticalsTreeCell;

import java.util.ResourceBundle;

public final class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        this.bind(IdenticalsTreeCell.class);
        this.bind(DuplicatesTreeCell.class);
        this.bind(Util.class).in(Scopes.SINGLETON);
        this.bind(ResourceBundle.class).toInstance(ResourceBundle.getBundle("strings"));
        this.bind(IConfigService.class).to(INIConfigService.class).in(Scopes.SINGLETON);
        this.bind(IBookInteractor.class).to(DefaultBookInteractor.class).in(Scopes.SINGLETON);
        this.bind(IBookDataSource.class).to(DefaultBookDataSource.class).in(Scopes.SINGLETON);
        this.bind(IFileDataSource.class).to(DefaultFileDataSource.class).in(Scopes.SINGLETON);
        this.bind(IBookRepository.class).to(DefaultBookRepository.class).in(Scopes.SINGLETON);
        this.bind(IConfigRepository.class).to(DefaultConfigRepository.class).in(Scopes.SINGLETON);
        this.bind(IConfigDataSource.class).to(DefaultConfigDataSource.class).in(Scopes.SINGLETON);
        this.bind(IConfigInteractor.class).to(DefaultConfigInteractor.class).in(Scopes.SINGLETON);
        this.bind(IISBNRetrieverService.class).to(DefaultISBNRetrieverService.class).in(Scopes.SINGLETON);
        this.bind(IContentRetrieverService.class).to(TikaContentRetrieverService.class).in(Scopes.SINGLETON);
    }
}
