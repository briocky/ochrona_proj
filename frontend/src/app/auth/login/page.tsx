'use client';
import DangerAlert from '@/component/alert/danger-alert';
import SuccessAlert from '@/component/alert/success-alert';
import LoginForm from '@/component/login-form/login-form';
import Logo from '@/component/logo/logo';
import { setAuthentication } from '@/redux/features/auth/auth-slice';
import { setToken } from '@/services/token.service';
import { getUser, setUser } from '@/services/user.service';
import { AuthResponse } from '@/type/auth/auth.types';
import { redirect, useRouter } from 'next/navigation';
import * as React from 'react';
import { useDispatch } from 'react-redux';

export default function Login() {
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
    setUser({
      id: response.id,
      email: response.email,
      firstName: response.firstName,
      lastName: response.lastName,
    });
    dispatch(setAuthentication(true));
    setTimeout(() => {
      router.replace('/');
    }, 2000);
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
    <section className="bg-gray-50 dark:bg-gray-900">
      <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
        <SuccessAlert
          title="Success"
          content="Login successfull. You will be redirected in a second."
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
              Sign in to your account
            </h1>
            <LoginForm
              handleFailure={handleFailure}
              handleSuccess={handleSuccess}
            />
          </div>
        </div>
      </div>
    </section>
  );
}
