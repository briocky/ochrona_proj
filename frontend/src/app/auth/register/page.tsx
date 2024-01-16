'use client';
import DangerAlert from '@/component/alert/danger-alert';
import SuccessAlert from '@/component/alert/success-alert';
import Logo from '@/component/logo/logo';
import RegisterForm from '@/component/register-form/register-form';
import { setAuthentication } from '@/redux/features/auth/auth-slice';
import { setUser } from '@/redux/features/user/user-slice';
import { setToken } from '@/services/token.service';
import { getUser } from '@/services/user.service';
import { AuthResponse } from '@/type/auth/auth.types';
import { redirect, useRouter } from 'next/navigation';
import * as React from 'react';
import { useDispatch } from 'react-redux';

export default function Register() {
  const router = useRouter();
  const [successAlertShown, setSuccessAlertShown] =
    React.useState<boolean>(false);
  const [dangerAlertShown, setDangerAlertShown] =
    React.useState<boolean>(false);
  const [errorContent, setErrorContent] = React.useState<string>('');
  const dispatch = useDispatch();

  const handleSuccess = (response: AuthResponse) => {
    setToken(response.accessToken);
    setSuccessAlertShown(true);
    window.scrollTo({ top: 0 });
    dispatch(setUser({
      id: response.id,
      email: response.email,
      firstName: response.firstName,
      lastName: response.lastName,
    }));
    setTimeout(() => {
      router.replace('/auth/register/confirm');
    }, 1500);
  };

  const handleFailure = (error: any) => {
    setErrorContent(error.response?.data || "Connection problem");
    setDangerAlertShown(true);
    window.scrollTo({ top: 0 });
    setTimeout(() => {
      setDangerAlertShown(false);
    }, 10000);
  };

  React.useLayoutEffect(() => {
    const usr = getUser();
    if (usr !== null) {
      redirect('/');
    }
  }, []);

  return (
    <section className="bg-gray-50 dark:bg-gray-900 pt-12 pb-10">
      <div className="flex flex-col items-center px-6 py-8 mx-auto lg:py-0">
        <SuccessAlert
          title="Success"
          content="Register successfull. Redirecting to account confirmation page..."
          disabled={successAlertShown}
        />
        <DangerAlert
          title="Danger"
          content={errorContent}
          disabled={dangerAlertShown}
        />
        <Logo />
        <div className="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
            <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
              Create an account
            </h1>
            <RegisterForm
              handleFailure={handleFailure}
              handleSuccess={handleSuccess}
            />
          </div>
        </div>
      </div>
    </section>
  );
}
