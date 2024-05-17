package com.dr.jjsembako.core.di

import com.dr.jjsembako.akun.domain.usecase.forgot.CheckAccountRecoveryActivationInteractor
import com.dr.jjsembako.akun.domain.usecase.forgot.CheckAccountRecoveryActivationUseCase
import com.dr.jjsembako.akun.domain.usecase.forgot.CheckAccountRecoveryAnswerInteractor
import com.dr.jjsembako.akun.domain.usecase.forgot.CheckAccountRecoveryAnswerUseCase
import com.dr.jjsembako.akun.domain.usecase.forgot.FetchAccountRecoveryQuestionByUsernameInteractor
import com.dr.jjsembako.akun.domain.usecase.forgot.FetchAccountRecoveryQuestionByUsernameUseCase
import com.dr.jjsembako.akun.domain.usecase.forgot.HandleUpdatePasswordFromRecoveryInteractor
import com.dr.jjsembako.akun.domain.usecase.forgot.HandleUpdatePasswordFromRecoveryUseCase
import com.dr.jjsembako.akun.domain.usecase.auth.LoginInteractor
import com.dr.jjsembako.akun.domain.usecase.auth.LoginUseCase
import com.dr.jjsembako.pelanggan.domain.usecase.FetchCustOrdersInteractor
import com.dr.jjsembako.pelanggan.domain.usecase.FetchCustOrdersUseCase
import com.dr.jjsembako.pelanggan.domain.usecase.FetchCustomersInteractor
import com.dr.jjsembako.pelanggan.domain.usecase.FetchCustomersUseCase
import com.dr.jjsembako.pelanggan.domain.usecase.FetchDetailCustomerInteractor
import com.dr.jjsembako.pelanggan.domain.usecase.FetchDetailCustomerUseCase
import com.dr.jjsembako.pelanggan.domain.usecase.HandleCreateCustomerInteractor
import com.dr.jjsembako.pelanggan.domain.usecase.HandleCreateCustomerUseCase
import com.dr.jjsembako.pelanggan.domain.usecase.HandleDeleteCustomerInteractor
import com.dr.jjsembako.pelanggan.domain.usecase.HandleDeleteCustomerUseCase
import com.dr.jjsembako.pelanggan.domain.usecase.HandleUpdateCustomerInteractor
import com.dr.jjsembako.pelanggan.domain.usecase.HandleUpdateCustomerUseCase
import com.dr.jjsembako.pesanan.domain.usecase.canceled.HandleCreateCanceledInteractor
import com.dr.jjsembako.pesanan.domain.usecase.canceled.HandleCreateCanceledUseCase
import com.dr.jjsembako.pesanan.domain.usecase.canceled.HandleDeleteCanceledInteractor
import com.dr.jjsembako.pesanan.domain.usecase.canceled.HandleDeleteCanceledUseCase
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.FetchOrderInteractor
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.FetchOrderUseCase
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.FetchOrdersInteractor
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.FetchOrdersUseCase
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleAddProductOrderInteractor
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleAddProductOrderUseCase
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleDeleteOrderInteractor
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleDeleteOrderUseCase
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleDeleteProductOrderInteractor
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleDeleteProductOrderUseCase
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleUpdatePaymentStatusInteractor
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleUpdatePaymentStatusUseCase
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleUpdateProductOrderInteractor
import com.dr.jjsembako.pesanan.domain.usecase.detail_order.HandleUpdateProductOrderUseCase
import com.dr.jjsembako.pesanan.domain.usecase.retur.HandleCreateReturInteractor
import com.dr.jjsembako.pesanan.domain.usecase.retur.HandleCreateReturUseCase
import com.dr.jjsembako.pesanan.domain.usecase.retur.HandleDeleteReturInteractor
import com.dr.jjsembako.pesanan.domain.usecase.retur.HandleDeleteReturUseCase
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchOmzetInteractor
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchOmzetUseCase
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchOrdersInteractor
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchOrdersMonthlyInteractor
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchOrdersMonthlyUseCase
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchOrdersUseCase
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchSelledProductInteractor
import com.dr.jjsembako.core.home.domain.usecase.HomeFetchSelledProductUseCase
import com.dr.jjsembako.pesanan.domain.usecase.create.FetchDetailSelectedCustInteractor
import com.dr.jjsembako.pesanan.domain.usecase.create.FetchDetailSelectedCustUseCase
import com.dr.jjsembako.pesanan.domain.usecase.create.FetchSelectCustInteractor
import com.dr.jjsembako.pesanan.domain.usecase.create.FetchSelectCustUseCase
import com.dr.jjsembako.pesanan.domain.usecase.create.HandleCreateOrderInteractor
import com.dr.jjsembako.pesanan.domain.usecase.create.HandleCreateOrderUseCase
import com.dr.jjsembako.performa.domain.usecase.FetchOmzetInteactor
import com.dr.jjsembako.performa.domain.usecase.FetchOmzetMonthlyInteractor
import com.dr.jjsembako.performa.domain.usecase.FetchOmzetMonthlyUseCase
import com.dr.jjsembako.performa.domain.usecase.FetchOmzetUseCase
import com.dr.jjsembako.performa.domain.usecase.FetchSelledProductMonthlyInteractor
import com.dr.jjsembako.performa.domain.usecase.FetchSelledProductMonthlyUseCase
import com.dr.jjsembako.akun.domain.usecase.recovery.ActivateAccountRecoveryInteractor
import com.dr.jjsembako.akun.domain.usecase.recovery.ActivateAccountRecoveryUseCase
import com.dr.jjsembako.akun.domain.usecase.change_pw.ChangePasswordInteractor
import com.dr.jjsembako.akun.domain.usecase.change_pw.ChangePasswordUseCase
import com.dr.jjsembako.akun.domain.usecase.recovery.DeactivateAccountRecoveryInteractor
import com.dr.jjsembako.akun.domain.usecase.recovery.DeactivateAccountRecoveryUseCase
import com.dr.jjsembako.akun.domain.usecase.recovery.GetAllRecoveryQuestionInteractor
import com.dr.jjsembako.akun.domain.usecase.recovery.GetAllRecoveryQuestionUseCase
import com.dr.jjsembako.akun.domain.usecase.recovery.GetDataAccountRecoveryInteractor
import com.dr.jjsembako.akun.domain.usecase.recovery.GetDataAccountRecoveryUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    /****************************
     * Account
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideLoginUseCase(loginInteractor: LoginInteractor): LoginUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideChangePasswordUseCase(changePasswordInteractor: ChangePasswordInteractor): ChangePasswordUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetAllRecoveryQuestionUseCase(getAllRecoveryQuestionInteractor: GetAllRecoveryQuestionInteractor): GetAllRecoveryQuestionUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetDataAccountRecoveryUseCase(getDataAccountRecoveryInteractor: GetDataAccountRecoveryInteractor): GetDataAccountRecoveryUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideActivateAccountRecoveryUseCase(activateAccountRecoveryInteractor: ActivateAccountRecoveryInteractor): ActivateAccountRecoveryUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDeactivateAccountRecoveryUseCase(deactivateAccountRecoveryInteractor: DeactivateAccountRecoveryInteractor): DeactivateAccountRecoveryUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCheckAccountRecoveryActivationUseCase(
        checkAccountRecoveryActivationInteractor: CheckAccountRecoveryActivationInteractor
    ): CheckAccountRecoveryActivationUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchAccountRecoveryQuestionByUsernameUseCase(
        fetchAccountRecoveryQuestionByUsernameInteractor: FetchAccountRecoveryQuestionByUsernameInteractor
    ): FetchAccountRecoveryQuestionByUsernameUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCheckAccountRecoveryAnswerUseCase(checkAccountRecoveryAnswerInteractor: CheckAccountRecoveryAnswerInteractor): CheckAccountRecoveryAnswerUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleUpdatePasswordFromRecoveryUseCase(
        hanldeUpdatePasswordFromRecoveryInteractor: HandleUpdatePasswordFromRecoveryInteractor
    ): HandleUpdatePasswordFromRecoveryUseCase

    /****************************
     * Customer
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideFetchCustomersUseCase(fetchCustomersInteractor: FetchCustomersInteractor): FetchCustomersUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchDetailCustomerUseCase(fetchDetailCustomerInteractor: FetchDetailCustomerInteractor): FetchDetailCustomerUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleCreateCustomerUseCase(handleCreateCustomerInteractor: HandleCreateCustomerInteractor): HandleCreateCustomerUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleUpdateCustomerUseCase(handleUpdateCustomerInteractor: HandleUpdateCustomerInteractor): HandleUpdateCustomerUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleDeleteCustomerUseCase(handleDeleteCustomerInteractor: HandleDeleteCustomerInteractor): HandleDeleteCustomerUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchCustOrdersUseCase(fetchCustOrdersInteractor: FetchCustOrdersInteractor): FetchCustOrdersUseCase

    /****************************
     * Order
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideHandleCreateOrderUseCase(handleCreateOrderInteractor: HandleCreateOrderInteractor): HandleCreateOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchSelectCustUseCase(fetchSelectCustInteractor: FetchSelectCustInteractor): FetchSelectCustUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchDetailSelectedCustUseCase(fetchDetailSelectedCustInteractor: FetchDetailSelectedCustInteractor): FetchDetailSelectedCustUseCase

    /****************************
     * History
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideFetchOrdersUseCase(fetchOrdersInteractor: FetchOrdersInteractor): FetchOrdersUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchOrderUseCase(fetchOrdesInteractor: FetchOrderInteractor): FetchOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleAddProductOrderUseCase(handleAddProductOrderInteractor: HandleAddProductOrderInteractor): HandleAddProductOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleUpdateProductOrderUseCase(handleUpdateProductOrderInteractor: HandleUpdateProductOrderInteractor): HandleUpdateProductOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleUpdatePaymentStatusUseCase(handleUpdatePaymentStatusInteractor: HandleUpdatePaymentStatusInteractor): HandleUpdatePaymentStatusUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleDeleteProductOrder(handleDeleteProductOrderInteractor: HandleDeleteProductOrderInteractor): HandleDeleteProductOrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleDeleteOrder(handleDeleteOrderInteractor: HandleDeleteOrderInteractor): HandleDeleteOrderUseCase

    /****************************
     * Canceled
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideHandleCreateCanceledUseCase(handleCreateCanceledInteractor: HandleCreateCanceledInteractor): HandleCreateCanceledUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleDeleteCanceledUseCase(handleDeleteCanceledInteractor: HandleDeleteCanceledInteractor): HandleDeleteCanceledUseCase

    /****************************
     * Retur
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideHandleCreateReturUseCase(handleCreateReturInteractor: HandleCreateReturInteractor): HandleCreateReturUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHandleDeleteReturUseCase(handleDeleteReturUseInteractor: HandleDeleteReturInteractor): HandleDeleteReturUseCase

    /****************************
     * Performance
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideFetchOmzetUseCase(fetchOmzetInteactor: FetchOmzetInteactor): FetchOmzetUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchOmzetMonthlyUseCase(fetchOmzetMonthlyInteractor: FetchOmzetMonthlyInteractor): FetchOmzetMonthlyUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchSelledProductMonthlyUseCase(fetchSelledProductMonthlyInteractor: FetchSelledProductMonthlyInteractor): FetchSelledProductMonthlyUseCase

    /****************************
     * Home
     ***************************/
    @Binds
    @ViewModelScoped
    abstract fun provideHomeFetchOmzetUseCase(homeFetchOmzetInteractor: HomeFetchOmzetInteractor): HomeFetchOmzetUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHomeFetchSelledProductUseCase(homeFetchSelledProductInteractor: HomeFetchSelledProductInteractor): HomeFetchSelledProductUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHomeFetchOrdersUseCase(homeFetchOrdersInteractor: HomeFetchOrdersInteractor): HomeFetchOrdersUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHomeFetchOrdersMonthlyUseCase(homeFetchOrdersMonthlyInteractor: HomeFetchOrdersMonthlyInteractor): HomeFetchOrdersMonthlyUseCase

}