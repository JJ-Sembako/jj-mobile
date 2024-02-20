package com.dr.jjsembako.di

import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryActivationInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryActivationUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryAnswerInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.CheckAccountRecoveryAnswerUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.FetchAccountRecoveryQuestionByUsernameInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.FetchAccountRecoveryQuestionByUsernameUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.HandleUpdatePasswordFromRecoveryInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.HandleUpdatePasswordFromRecoveryUseCase
import com.dr.jjsembako.feature_auth.domain.usecase.LoginInteractor
import com.dr.jjsembako.feature_auth.domain.usecase.LoginUseCase
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustOrdersInteractor
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustOrdersUseCase
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustomersInteractor
import com.dr.jjsembako.feature_customer.domain.usecase.FetchCustomersUseCase
import com.dr.jjsembako.feature_customer.domain.usecase.FetchDetailCustomerInteractor
import com.dr.jjsembako.feature_customer.domain.usecase.FetchDetailCustomerUseCase
import com.dr.jjsembako.feature_customer.domain.usecase.HandleCreateCustomerInteractor
import com.dr.jjsembako.feature_customer.domain.usecase.HandleCreateCustomerUseCase
import com.dr.jjsembako.feature_customer.domain.usecase.HandleDeleteCustomerInteractor
import com.dr.jjsembako.feature_customer.domain.usecase.HandleDeleteCustomerUseCase
import com.dr.jjsembako.feature_customer.domain.usecase.HandleUpdateCustomerInteractor
import com.dr.jjsembako.feature_customer.domain.usecase.HandleUpdateCustomerUseCase
import com.dr.jjsembako.feature_history.domain.usecase.canceled.HandleCreateCanceledInteractor
import com.dr.jjsembako.feature_history.domain.usecase.canceled.HandleCreateCanceledUseCase
import com.dr.jjsembako.feature_history.domain.usecase.canceled.HandleDeleteCanceledInteractor
import com.dr.jjsembako.feature_history.domain.usecase.canceled.HandleDeleteCanceledUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderInteractor
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrdersInteractor
import com.dr.jjsembako.feature_history.domain.usecase.order.FetchOrdersUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleAddProductOrderInteractor
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleAddProductOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleDeleteOrderInteractor
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleDeleteOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleDeleteProductOrderInteractor
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleDeleteProductOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleUpdatePaymentStatusInteractor
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleUpdatePaymentStatusUseCase
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleUpdateProductOrderInteractor
import com.dr.jjsembako.feature_history.domain.usecase.order.HandleUpdateProductOrderUseCase
import com.dr.jjsembako.feature_history.domain.usecase.retur.HandleCreateReturInteractor
import com.dr.jjsembako.feature_history.domain.usecase.retur.HandleCreateReturUseCase
import com.dr.jjsembako.feature_history.domain.usecase.retur.HandleDeleteReturInteractor
import com.dr.jjsembako.feature_history.domain.usecase.retur.HandleDeleteReturUseCase
import com.dr.jjsembako.feature_order.domain.usecase.FetchDetailSelectedCustInteractor
import com.dr.jjsembako.feature_order.domain.usecase.FetchDetailSelectedCustUseCase
import com.dr.jjsembako.feature_order.domain.usecase.FetchSelectCustInteractor
import com.dr.jjsembako.feature_order.domain.usecase.FetchSelectCustUseCase
import com.dr.jjsembako.feature_order.domain.usecase.HandleCreateOrderInteractor
import com.dr.jjsembako.feature_order.domain.usecase.HandleCreateOrderUseCase
import com.dr.jjsembako.feature_performance.domain.usecase.FetchOmzetInteactor
import com.dr.jjsembako.feature_performance.domain.usecase.FetchOmzetMonthlyInteractor
import com.dr.jjsembako.feature_performance.domain.usecase.FetchOmzetMonthlyUseCase
import com.dr.jjsembako.feature_performance.domain.usecase.FetchOmzetUseCase
import com.dr.jjsembako.feature_performance.domain.usecase.FetchSelledProductMonthlyInteractor
import com.dr.jjsembako.feature_performance.domain.usecase.FetchSelledProductMonthlyUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.ActivateAccountRecoveryInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.ActivateAccountRecoveryUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.ChangePasswordInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.ChangePasswordUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.DeactivateAccountRecoveryInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.DeactivateAccountRecoveryUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.GetAllRecoveryQuestionInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.GetAllRecoveryQuestionUseCase
import com.dr.jjsembako.feature_setting.domain.usecase.GetDataAccountRecoveryInteractor
import com.dr.jjsembako.feature_setting.domain.usecase.GetDataAccountRecoveryUseCase
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
    abstract fun provideFetchOmzetMonthlyUseCase(fetchOmzetMonthlyIteractor: FetchOmzetMonthlyInteractor): FetchOmzetMonthlyUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideFetchSelledProductMonthlyUseCase(fetchSelledProductMonthlyInteractor: FetchSelledProductMonthlyInteractor): FetchSelledProductMonthlyUseCase

}